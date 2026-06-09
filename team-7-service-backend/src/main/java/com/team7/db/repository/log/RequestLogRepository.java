package com.team7.db.repository.log;

import com.team7.db.model.log.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

}


