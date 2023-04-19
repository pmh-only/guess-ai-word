import { type ReactNode, type FC } from 'react'
import style from './style.module.scss'

interface Props {
  children: ReactNode
}

const Container: FC<Props> = ({ children }) =>
  <div className={style.container}>
    <div className={style.content}>
      {children}
    </div>
  </div>

export default Container
