package com.incede.nbfc.notification.repository;

import com.incede.nbfc.notification.domain.Category;
import com.incede.nbfc.notification.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Integer>
{

}
