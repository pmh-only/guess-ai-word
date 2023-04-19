import { useState, type FC } from 'react'
import GameTab from '../GameTab'
import CreditTab from '../CreditTab'
import BoardTab from '../BoardTab'

import style from './style.module.scss'
import TabItem from './TabItem'
import { FaInfoCircle, FaAward, FaGamepad } from 'react-icons/fa'

const BottomTab: FC = () => {
  const [tabIndex, setTabIndex] = useState(0)

  return (
    <>
      <main className={style.main}>
        <TabItem show={tabIndex === 1}><CreditTab /></TabItem>
        <TabItem lowZ show={tabIndex === 0}><GameTab /></TabItem>
        <TabItem show={tabIndex === 2}><BoardTab /></TabItem>
      </main>
      <nav className={style.bottomTab}>
        <button
          className={tabIndex === 1 ? style.enabled : ''}
          onClick={() => { setTabIndex(1) }}>
          <FaInfoCircle />
          <label>게임 정보</label>
        </button>
        <button
          className={tabIndex === 0 ? style.enabled : ''}
          onClick={() => { setTabIndex(0) }}>
          <FaGamepad />
          <label>홈</label>
        </button>
        <button
          className={tabIndex === 2 ? style.enabled : ''}
          onClick={() => { setTabIndex(2) }}>
          <FaAward />
          <label>리더보드</label>
        </button>
      </nav>
    </>
  )
}

export default BottomTab
