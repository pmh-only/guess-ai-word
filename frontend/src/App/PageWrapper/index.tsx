import { type FC, type ReactNode } from 'react'
import { motion } from 'framer-motion'

import style from './style.module.scss'

interface Props {
  children: ReactNode
}

const PageWrapper: FC<Props> = ({ children }) =>
  <motion.div
    className={style.container}
    transition={{ type: 'spring', damping: 10, stiffness: 50 }}
    initial={{ translateX: 200, opacity: -1 }}
    animate={{ translateX: 0, opacity: 1 }}
    exit={{ translateX: 200, opacity: -1 }}>
      {children}
    </motion.div>

export default PageWrapper
