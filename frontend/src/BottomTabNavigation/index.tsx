import { type FC } from 'react'
import style from './style.module.scss'
import { MdGames, MdInfo, MdScore } from 'react-icons/md'
import { useLocation, useNavigate } from 'react-router-dom'

import { AnimatePresence, motion } from 'framer-motion'

const BottomTabNavigation: FC = () => {
  const location = useLocation()
  const navigate = useNavigate()

  return (
    <AnimatePresence>
      {!location.pathname.startsWith('/ingame') && (
        <motion.nav
          transition={{ bounce: 0, duration: 0.5 }}
          initial={{ translateY: 80 }}
          animate={{ translateY: 0 }}
          exit={{ translateY: 80 }}
          className={style.bottomTab}>
          <motion.button
            transition={{ duration: 0.2 }}
            whileTap={{ backgroundColor: 'var(--main-secondary)' }}
            className={location.pathname === '/credits' ? style.enabled : ''}
            onClick={() => { navigate('/credits') }}>
            <MdInfo />
            <label>게임 정보</label>
          </motion.button>
          <motion.button
            transition={{ duration: 0.2 }}
            whileTap={{ backgroundColor: 'var(--main-secondary)' }}
            className={location.pathname === '/' ? style.enabled : ''}
            onClick={() => { navigate('/') }}>
            <MdGames />
            <label>홈</label>
          </motion.button>
          <motion.button
            transition={{ duration: 0.2 }}
            whileTap={{ backgroundColor: 'var(--main-secondary)' }}
            className={location.pathname === '/boards' ? style.enabled : ''}
            onClick={() => { navigate('/boards') }}>
            <MdScore />
            <label>리더보드</label>
          </motion.button>
        </motion.nav>
      )}
    </AnimatePresence>
  )
}

export default BottomTabNavigation
