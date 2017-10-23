package me.robnette.spring_angular.task;

import me.robnette.spring_angular.dao.TokenExpire;
import me.robnette.spring_angular.repository.TokenExpireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
public class ScheduledTasks {

    @Autowired
    private TokenExpireRepository tokenRepository;

    @Scheduled(fixedRate = 3600000)
    public void keepAlive() {
        List<TokenExpire> tokenExpires = tokenRepository.findAllBefore(new Date());
        for(TokenExpire tokenExpire : tokenExpires){
            tokenRepository.delete(tokenExpire);
        }
    }
}
