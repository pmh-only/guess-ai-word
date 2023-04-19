import { type FC, type ReactNode } from 'react'
import { AnimatePresence } from 'framer-motion'

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
        {children}
      </article>
    )}
  </AnimatePresence>

export default TabItem
