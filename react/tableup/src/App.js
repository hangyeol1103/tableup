import Header from './Header'
import Footer from './Footer';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import Main from './Main';
import MyPage from './pages/MyPage';

function App() {
  
  return(
    <BrowserRouter>
      <Header/>
        <Routes>
          <Route path='/' element={<Main/>} />
          <Route path='/user/mypage' element={<MyPage/>} />
        </Routes>
      <Footer/>
    </BrowserRouter>
  )
}

export default App;