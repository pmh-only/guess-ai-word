import { type FC } from 'react'
import { motion } from 'framer-motion'

const GameTab: FC = () =>
  <motion.div exit={{ opacity: 0 }}>
    저는 게임탭입니다.
  </motion.div>

export default GameTab
