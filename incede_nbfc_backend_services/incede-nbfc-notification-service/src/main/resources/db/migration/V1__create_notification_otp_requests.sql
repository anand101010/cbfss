-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS notification_service_schema;

-- Table: notification_service_schema.otp_requests
CREATE TABLE IF NOT EXISTS notification_service_schema.otp_requests (
	request_id           BIGSERIAL PRIMARY KEY,
	identity             UUID NOT NULL UNIQUE,
	tenant_id            INTEGER NOT NULL,
	branch_code          VARCHAR(255) NOT NULL,
	customer_identity    UUID,
	purpose              VARCHAR(40) NOT NULL,
	channel              VARCHAR(16) NOT NULL,
	msisdn               VARCHAR(255) NOT NULL,
	active_slot_key      VARCHAR(64) NOT NULL,
	idempotency_key      VARCHAR(64),
	otp_hash             TEXT NOT NULL,
	otp_salt             BYTEA NOT NULL,
	otp_length           SMALLINT NOT NULL,
	expires_at           TIMESTAMPTZ NOT NULL,
	max_attempts         SMALLINT NOT NULL,
	attempt_count        SMALLINT NOT NULL,
	status               VARCHAR(20) NOT NULL,
	provider_message_id  VARCHAR(255),
	metadata_json        TEXT,
	created_at           TIMESTAMPTZ NOT NULL,
	updated_at           TIMESTAMPTZ NOT NULL
);

-- Indexes to support lookups and housekeeping
CREATE INDEX IF NOT EXISTS idx_otp_requests_identity ON notification_service_schema.otp_requests (identity);
CREATE INDEX IF NOT EXISTS idx_otp_requests_tenant_branch ON notification_service_schema.otp_requests (tenant_id, branch_code);
CREATE INDEX IF NOT EXISTS idx_otp_requests_slot_key ON notification_service_schema.otp_requests (active_slot_key);
CREATE INDEX IF NOT EXISTS idx_otp_requests_status ON notification_service_schema.otp_requests (status);
CREATE INDEX IF NOT EXISTS idx_otp_requests_expires_at ON notification_service_schema.otp_requests (expires_at);
CREATE INDEX IF NOT EXISTS idx_otp_requests_idempotency ON notification_service_schema.otp_requests (tenant_id, branch_code, idempotency_key);


-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS notification;

-- Table: notification.otp_requests
CREATE TABLE IF NOT EXISTS notification.otp_requests (
	request_id           BIGSERIAL PRIMARY KEY,
	identity             UUID NOT NULL UNIQUE,
	tenant_id            INTEGER NOT NULL,
	branch_code          VARCHAR(255) NOT NULL,
	customer_identity    UUID,
	purpose              VARCHAR(40) NOT NULL,
	channel              VARCHAR(16) NOT NULL,
	msisdn               VARCHAR(255) NOT NULL,
	active_slot_key      VARCHAR(64) NOT NULL,
	idempotency_key      VARCHAR(64),
	otp_hash             TEXT NOT NULL,
	otp_salt             BYTEA NOT NULL,
	otp_length           SMALLINT NOT NULL,
	expires_at           TIMESTAMPTZ NOT NULL,
	max_attempts         SMALLINT NOT NULL,
	attempt_count        SMALLINT NOT NULL,
	status               VARCHAR(20) NOT NULL,
	provider_message_id  VARCHAR(255),
	metadata_json        TEXT,
	created_at           TIMESTAMPTZ NOT NULL,
	updated_at           TIMESTAMPTZ NOT NULL
);

-- Indexes to support lookups and housekeeping
CREATE INDEX IF NOT EXISTS idx_otp_requests_identity ON notification.otp_requests (identity);
CREATE INDEX IF NOT EXISTS idx_otp_requests_tenant_branch ON notification.otp_requests (tenant_id, branch_code);
CREATE INDEX IF NOT EXISTS idx_otp_requests_slot_key ON notification.otp_requests (active_slot_key);
CREATE INDEX IF NOT EXISTS idx_otp_requests_status ON notification.otp_requests (status);
CREATE INDEX IF NOT EXISTS idx_otp_requests_expires_at ON notification.otp_requests (expires_at);
CREATE INDEX IF NOT EXISTS idx_otp_requests_idempotency ON notification.otp_requests (tenant_id, branch_code, idempotency_key);



-- =========================================================
-- NOTIFICATION TEMPLATES (PostgreSQL)
-- =========================================================

-- 1) Master catalog of business templates (one row per template code)
CREATE TABLE IF NOT EXISTS notification.template_catalog (
  template_id       SERIAL PRIMARY KEY,
  tenant_id         INT NOT NULL REFERENCES tenant.tenant_details(tenantid),
  code              VARCHAR(64) NOT NULL,
  name              VARCHAR(120) NOT NULL,
  description       TEXT,
  category          VARCHAR(32) NOT NULL CHECK (category IN ('OTP','TRANSACTIONAL','PROMOTIONAL','ALERT')),
  default_channel   VARCHAR(16) NOT NULL CHECK (default_channel IN ('SMS','EMAIL','WHATSAPP','PUSH')),
  version           INT NOT NULL DEFAULT 1,
  is_active         BOOLEAN NOT NULL DEFAULT TRUE,
  created_by        INT NOT NULL REFERENCES users.users(user_id),
  created_at        TIMESTAMPTZ NOT NULL,
  updated_by        INT REFERENCES users.users(user_id),
  updated_at        TIMESTAMPTZ NOT NULL,
  is_del            BOOLEAN NOT NULL DEFAULT FALSE,
  identity          UUID NOT NULL UNIQUE
);

-- Tenanted uniqueness on code
CREATE UNIQUE INDEX IF NOT EXISTS uq_template_catalog_tenant_code
  ON notification.template_catalog (tenant_id, code)
  WHERE is_del = FALSE;

-- 2) Channel & language specific content variants for a template
CREATE TABLE IF NOT EXISTS notification.template_contents (
  template_content_id SERIAL PRIMARY KEY,
  template_id         INT NOT NULL REFERENCES notification.template_catalog(template_id),
  channel             VARCHAR(16) NOT NULL CHECK (channel IN ('SMS','EMAIL','WHATSAPP','PUSH')),
  language_code       VARCHAR(10) NOT NULL,      -- e.g., 'en', 'hi', 'ta-IN'
  subject             TEXT,                      -- used for EMAIL/PUSH
  body                TEXT NOT NULL,             -- placeholder-enabled
  footer              TEXT,
  max_length          INT,
  sender_id           VARCHAR(20),               -- e.g., 'INDOTP' for SMS
  provider_params     JSONB,                     -- vendor-specific options
  dlt_principal_id    VARCHAR(50),               -- optional (India DLT)
  dlt_template_id     VARCHAR(50),               -- optional (India DLT)
  checksum            VARCHAR(64),
  is_active           BOOLEAN NOT NULL DEFAULT TRUE,
  created_by          INT NOT NULL REFERENCES users.users(user_id),
  created_at          TIMESTAMPTZ NOT NULL,
  updated_by          INT REFERENCES users.users(user_id),
  updated_at          TIMESTAMPTZ NOT NULL,
  is_del              BOOLEAN NOT NULL DEFAULT FALSE,
  identity            UUID NOT NULL UNIQUE
);

-- Only one active variant per (template, channel, language)
CREATE UNIQUE INDEX IF NOT EXISTS uq_template_content_active
  ON notification.template_contents (template_id, channel, language_code)
  WHERE is_active = TRUE AND is_del = FALSE;

-- 3) OTP REQUESTS: link to template(s); remove language/params/preview as requested
--    (Adjust schema/table/column names if yours differ.)

-- Add FKs to tie a request to the chosen template + content variant
ALTER TABLE notification.otp_requests
  ADD COLUMN IF NOT EXISTS template_id INT REFERENCES notification.template_catalog(template_id),
  ADD COLUMN IF NOT EXISTS template_content_id INT REFERENCES notification.template_contents(template_content_id);

-- Remove no-longer-required columns if they were added previously
ALTER TABLE notification.otp_requests
  DROP COLUMN IF EXISTS language_code,
  DROP COLUMN IF EXISTS params_json,
  DROP COLUMN IF EXISTS rendered_preview_masked;



  CREATE TABLE IF NOT EXISTS notification.template_catalog (
    template_catalog_id SERIAL PRIMARY KEY,
    tenant_id          INT NOT NULL ,
    code              VARCHAR(64) NOT NULL,
    name              VARCHAR(120) NOT NULL,
    description       TEXT,
    category          VARCHAR(32) NOT NULL CHECK (category IN ('OTP','TRANSACTIONAL','PROMOTIONAL','ALERT')),
    default_channel   VARCHAR(16) NOT NULL CHECK (default_channel IN ('SMS','EMAIL','WHATSAPP','PUSH')),
    is_active         BOOLEAN NOT NULL DEFAULT TRUE,
    created_by        INT NOT NULL ,
    created_at        TIMESTAMPTZ NOT NULL,
    updated_by        INT ,
    updated_at        TIMESTAMPTZ NOT NULL,
    is_del            BOOLEAN NOT NULL DEFAULT FALSE,
    identity          UUID NOT NULL UNIQUE
  );
  CREATE TABLE IF NOT EXISTS notification.template_contents (
    template_content_id SERIAL PRIMARY KEY,
    template_catalog_id          INT NOT NULL,
    channel             VARCHAR(16) NOT NULL CHECK (channel IN ('SMS','EMAIL','WHATSAPP','PUSH')),
    language_code       VARCHAR(10) NOT NULL,      -- e.g., 'en', 'hi', 'ta-IN'
    purpose             VARCHAR(255) NOT NULL,
    subject             TEXT,                      -- used for EMAIL/PUSH
    body                TEXT NOT NULL,             -- placeholder-enabled
    footer              TEXT,
    max_length          INT,             -- e.g., 'INDOTP' for SMS
    sender_id           VARCHAR(20),               -- e.g., 'INDOTP' for SMS
    provider_params     JSONB,                     -- vendor-specific options
    dlt_principal_id    VARCHAR(50),               -- optional (India DLT)
    dlt_template_id     VARCHAR(50),               -- optional (India DLT)
    checksum            VARCHAR(64),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_by          INT NOT NULL ,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_by          INT,
    updated_at          TIMESTAMPTZ NOT NULL,
    is_del              BOOLEAN NOT NULL DEFAULT FALSE,
    identity            UUID NOT NULL UNIQUE
  );
 CONSTRAINT uq_template_channel_purpose UNIQUE (template_catalog_id, channel, purpose)
);


\\\\\\\\\\\\\\\\\\\\\\\\\\\\\updated\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
-- notification.categories definition

-- Drop table

-- DROP TABLE categories;

CREATE TABLE categories (
	category_id serial4 NOT NULL,
	tenant_id int4 NOT NULL,
	name varchar(50) NOT NULL,
	description text NULL,
	is_active bool DEFAULT true NOT NULL,
	is_del bool DEFAULT false NOT NULL,
	created_by int4 NOT NULL,
	created_at timestamptz DEFAULT now() NOT NULL,
	updated_by int4 NULL,
	updated_at timestamptz NULL,
	"identity" uuid DEFAULT gen_random_uuid() NOT NULL,
	CONSTRAINT categories_identity_key UNIQUE ("identity"),
	CONSTRAINT categories_name_key UNIQUE (name),
	CONSTRAINT categories_pkey PRIMARY KEY (category_id)
);



-- notification.channels definition

-- Drop table

-- DROP TABLE channels;

CREATE TABLE channels (
	channel_id serial4 NOT NULL,
	tenant_id int4 NOT NULL,
	name varchar(100) NOT NULL,
	description text NULL,
	is_active bool DEFAULT true NOT NULL,
	is_delete bool DEFAULT false NOT NULL,
	created_by int4 NOT NULL,
	created_at timestamptz DEFAULT now() NOT NULL,
	updated_by int4 NULL,
	updated_at timestamptz NULL,
	"identity" uuid DEFAULT gen_random_uuid() NOT NULL,
	CONSTRAINT channels_identity_key UNIQUE ("identity"),
	CONSTRAINT channels_name_key UNIQUE (name),
	CONSTRAINT channels_pkey PRIMARY KEY (channel_id)
);



-- notification.purposes definition

-- Drop table

-- DROP TABLE purposes;

CREATE TABLE purposes (
	purpose_id serial4 NOT NULL,
	tenant_id int4 NOT NULL,
	name varchar(100) NOT NULL,
	description text NULL,
	is_active bool DEFAULT true NOT NULL,
	is_del bool DEFAULT false NOT NULL,
	created_by int4 NOT NULL,
	created_at timestamptz DEFAULT now() NOT NULL,
	updated_by int4 NULL,
	updated_at timestamptz NULL,
	"identity" uuid DEFAULT gen_random_uuid() NOT NULL,
	CONSTRAINT purposes_identity_key UNIQUE ("identity"),
	CONSTRAINT purposes_name_key UNIQUE (name),
	CONSTRAINT purposes_pkey PRIMARY KEY (purpose_id)
);


-- notification.otp_requests definition

-- Drop table

-- DROP TABLE otp_requests;

CREATE TABLE otp_requests (
	otp_request_id bigserial NOT NULL,
	tenant_id int4 NOT NULL,
	branch_code varchar(255) NOT NULL,
	customer_identity int4 NULL,
	template_catalog_id int4 NOT NULL REFERENCES template_catalog.template_catalog_id(template_catalog_id),
	template_content_id int4 NOT NULL REFERENCES template_contents.template_content_id(template_content_id),
	msisdn varchar(255) NOT NULL,
	active_slot_key varchar(64) NOT NULL,
	idempotency_key varchar(64) NULL,
	otp_hash text NOT NULL,
	otp_salt bytea NOT NULL,
	otp_length int2 NOT NULL,
	expires_at timestamptz NOT NULL,
	max_attempts int2 NOT NULL,
	attempt_count int2 NOT NULL,
	status varchar(20) NOT NULL,
	provider_request_json text NULL,
	provider_response_id varchar(255) NULL,
	created_by int4 NULL,
	created_at timestamptz NOT NULL,
	updated_by int4 NULL,
	updated_at timestamptz NULL,
	"identity" uuid NOT NULL,
	CONSTRAINT otp_requests_identity_key UNIQUE ("identity"),
	CONSTRAINT otp_requests_pkey PRIMARY KEY (otp_request_id)
);



-- notification.template_catalog definition

-- Drop table

-- DROP TABLE template_catalog;

CREATE TABLE template_catalog (
	template_catalog_id serial4 NOT NULL,
	tenant_id int4 NOT NULL,
	code varchar(64) NOT NULL,
	name varchar(120) NOT NULL,
	description text NULL,
	category_id int4 NOT NULL REFERENCES categories.category_id(category_id),
	channel_id int4 NOT NULL REFERENCES channels.channel_id(channel_id),
	is_active bool DEFAULT true NOT NULL,
	created_by int4 NOT NULL,
	created_at timestamptz NOT NULL,
	updated_by int4 NULL,
	updated_at timestamptz NULL,
	is_delete bool DEFAULT false NOT NULL,
	"identity" uuid NOT NULL,
	CONSTRAINT template_catalog_identity_key UNIQUE ("identity"),
	CONSTRAINT template_catalog_pkey PRIMARY KEY (template_catalog_id)
);

-- notification.template_contents definition

-- Drop table

-- DROP TABLE template_contents;

CREATE TABLE template_contents (
	template_content_id serial4 NOT NULL,
	template_catalog_id int4 NOT NULL,
	channel_id int4 NOT NULL REFERENCES channels.channel_id(channel_id),
	language_code varchar(10) NOT NULL,
	purpose_id int4 NOT NULL REFERENCES  purposes.purpose_id(purpose_id),
	subject text NULL,
	body text NOT NULL,
	footer text NULL,
	is_active bool DEFAULT true NOT NULL,
	created_by int4 NOT NULL,
	created_at timestamptz NOT NULL,
	updated_by int4 NULL,
	updated_at timestamptz NULL,
	is_delete bool DEFAULT false NOT NULL,
	"identity" uuid NOT NULL,
	CONSTRAINT template_contents_identity_key UNIQUE ("identity"),
	CONSTRAINT template_contents_pkey PRIMARY KEY (template_content_id),
	CONSTRAINT uq_template_channel_purpose UNIQUE (template_catalog_id,channel_id,purpose_id)
);



