import { type FC } from 'react'
import style from './style.module.scss'
import { FaInfoCircle, FaAward, FaGamepad } from 'react-icons/fa'
import { useLocation, useNavigate } from 'react-router-dom'

import { motion } from 'framer-motion'

const BottomTabNavigation: FC = () => {
  const location = useLocation()
  const navigate = useNavigate()

  return (
    <nav className={style.bottomTab}>
      <motion.button
        transition={{ duration: 0.2 }}
        whileTap={{ backgroundColor: 'var(--main-secondary)' }}
        className={location.pathname === '/credits' ? style.enabled : ''}
        onClick={() => { navigate('/credits') }}>
        <FaInfoCircle />
        <label>게임 정보</label>
      </motion.button>
      <motion.button
        transition={{ duration: 0.2 }}
        whileTap={{ backgroundColor: 'var(--main-secondary)' }}
        className={location.pathname === '/' ? style.enabled : ''}
        onClick={() => { navigate('/') }}>
        <FaGamepad />
        <label>홈</label>
      </motion.button>
      <motion.button
        transition={{ duration: 0.2 }}
        whileTap={{ backgroundColor: 'var(--main-secondary)' }}
        className={location.pathname === '/boards' ? style.enabled : ''}
        onClick={() => { navigate('/boards') }}>
        <FaAward />
        <label>리더보드</label>
      </motion.button>
    </nav>
  )
}

export default BottomTabNavigation
