import { type FC } from 'react'
import { MdClose } from 'react-icons/md'

import style from './style.module.scss'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'

interface Props {
  title: string
}

const TitleBar: FC<Props> = ({ title }) =>
  <nav className={style.titleBar}>
    <Link to="/">
      <motion.button
        whileTap={{ backgroundColor: 'var(--main-secondary)' }}
        className={style.backBtn}>
        <MdClose size={25} />
      </motion.button>
    </Link>
    <h2>{title}</h2>
  </nav>

export default TitleBar
