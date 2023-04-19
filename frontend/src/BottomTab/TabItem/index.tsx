import { type FC, type ReactNode } from 'react'
import { AnimatePresence, motion } from 'framer-motion'

import style from './style.module.scss'

interface Props {
  children: ReactNode
  show: boolean
  lowZ?: boolean
}

const TabItem: FC<Props> = ({ children, show, lowZ }) =>
  <AnimatePresence>
    {show && (
      <article style={{ zIndex: lowZ === true ? -10 : 0 }} className={style.tabItem}>
        <motion.div
          className={style.container}
          transition={{ type: 'spring', damping: 10, stiffness: 50 }}
          initial={{ translateX: 200, opacity: -1 }}
          animate={{ translateX: 0, opacity: 1 }}
          exit={{ translateX: 200, opacity: -1 }}>
          {children}
        </motion.div>
      </article>
    )}
  </AnimatePresence>

export default TabItem
