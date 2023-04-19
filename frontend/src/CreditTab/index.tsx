import { type FC } from 'react'
import { motion } from 'framer-motion'

import style from './style.module.scss'

const CreditTab: FC = () =>
  <motion.div
    className={style.container}
    transition={{ bounce: 0 }}
    initial={{ translateX: -500 }}
    animate={{ translateX: 0 }}
    exit={{ translateX: -500, opacity: 0 }}>

    안녕하세요 전 크래딧 텝입ㄴ디ㅏ
  </motion.div>

export default CreditTab
