import React, { useEffect, useState } from 'react';
import axiosInstance from '../api/axiosInstance';
import './MyPage.css'; // 선택 사항 (없으면 제거 가능)

function MyPage() {
  const [userInfo, setUserInfo] = useState(null);
  const [reservations, setReservations] = useState([]);

  useEffect(() => {
    // 사용자 정보 가져오기
    axiosInstance.get('/user/info')
      .then(res => setUserInfo(res.data))
      .catch(err => console.error('유저 정보 오류:', err));

    // 예약 내역 가져오기
    axiosInstance.get('/reservation/my')
      .then(res => setReservations(res.data))
      .catch(err => console.error('예약 목록 오류:', err));
  }, []);

  return (
    <div className="mypage-container">
      <h1>마이페이지</h1>

      {/* 사용자 정보 출력 */}
      {userInfo ? (
        <div className="user-info">
          <p><strong>이름:</strong> {userInfo.name}</p>
          <p><strong>이메일:</strong> {userInfo.email}</p>
        </div>
      ) : (
        <p>사용자 정보를 불러오는 중입니다...</p>
      )}

      {/* 예약 내역 출력 */}
      <h2>예약 내역</h2>
      <ul className="reservation-list">
        {reservations.length > 0 ? (
          reservations.map(res => (
            <li key={res.res_num}>
              <span>
                {res.restaurantName} - {res.res_time} ({res.res_person}명)
              </span>
              {res.reviewed ? (
                <span className="done"> ✅ 리뷰 완료</span>
              ) : (
                <button
                  onClick={() =>
                    window.location.href = `/review/write/${res.res_rt_num}`
                  }>
                  리뷰 작성
                </button>
              )}
            </li>
          ))
        ) : (
          <li>예약 내역이 없습니다.</li>
        )}
      </ul>
    </div>
  );
}

export default MyPage;
