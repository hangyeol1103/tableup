package kr.kh.tableup.model.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kr.kh.tableup.service.ManagerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpirationScheduler {
	private final ManagerService managerService;

	@Scheduled(cron ="0 59 23 * * ?")
	public void processExpiredDate(){
		managerService.expireOldCoupons();
	}
}
