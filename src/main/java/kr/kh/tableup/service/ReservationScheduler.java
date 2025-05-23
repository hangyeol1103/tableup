package kr.kh.tableup.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.kh.tableup.dao.BusinessHourDAO;
import kr.kh.tableup.dao.ReservationDAO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.ReservationVO;

@Component
public class ReservationScheduler {

    @Autowired
    ReservationDAO reservationDAO;

    @Autowired
    BusinessHourDAO businessHourDAO;

    /** ⏱️ 매 5분마다 예약 만료 처리 */
    @Scheduled(cron = "0 */5 * * * *") // 5분마다 실행
    public void updateExpiredReservations() {
        List<ReservationVO> expiredList = reservationDAO.selectExpiredReservations(LocalDateTime.now());

        for (ReservationVO res : expiredList) {
            List<BusinessHourVO> bhList = reservationDAO.selectOverlapHours(
                res.getRes_rt_num(), res.getRes_time(), res.getRes_end_time());

            for (BusinessHourVO bh : bhList) {
                int updated = bh.getBh_seat_current() - res.getRes_person();
                if (updated < 0) updated = 0;

                businessHourDAO.updateCurrentSeat(bh.getBh_num(), updated);
            }

            reservationDAO.updateReservationState(res.getRes_num(), 1);
        }
    }
}
