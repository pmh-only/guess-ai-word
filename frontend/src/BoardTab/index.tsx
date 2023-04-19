import { type FC } from 'react'
import { motion } from 'framer-motion'

import style from './style.module.scss'

const BoardTab: FC = () =>
  <motion.div
    className={style.container}
    transition={{ bounce: 0 }}
    initial={{ translateX: 500 }}
    animate={{ translateX: 0 }}
    exit={{ translateX: 500, opacity: 0 }}>
    안녕하세요 저는 스코어보드/게임 기록 탭이에요
  </motion.div>

export default BoardTab
