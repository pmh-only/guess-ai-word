import { type FC } from 'react'
import style from './style.module.scss'
import TitleBar from '../TitleBar'

const CreditTab: FC = () =>
  <>
    <TitleBar isFreepass title="게임 정보" />
    <ul className={style.credit}>
      <li><img src="/credit/uiux.webp" /><div className={style.content}><h2>UI/UX Design</h2><p>Minhyeok Park</p></div></li>
      <li><img src="/credit/frontend.webp" /><div className={style.content}><h2>React Frontend</h2><p>Minhyeok Park</p></div></li>
      <li><img src="/credit/backend.webp" /><div className={style.content}><h2>Spring Backend</h2><p>Minhyeok Park</p></div></li>
      <li><img src="/credit/prompt.webp" /><div className={style.content}><h2>Prompt Eng.</h2><p>Minhyeok Park</p></div></li>
      <li><img src="/credit/openai.webp" className={style.openai} /><div className={style.content}><h2>Powered by</h2><p>ChatGPT API</p></div></li>
      <li><img src="/credit/designref.webp" /><div className={style.content}><h2>Design ref. by</h2><p>Google Material Design 3</p></div></li>
      <li><div className={style.content}><h2>Copyright</h2><p>&copy; 2023. Minhyeok Park</p><a target='_blank' href="https://github.com/pmh-only/guess-ai-word/blob/main/LICENSE.md" rel="noreferrer">Read more</a></div></li>
      <li><div className={style.content}><h2>Source Code</h2><p>pmh-only/guessaiword</p><a target='_blank' href="https://github.com/pmh-only/guess-ai-word" rel="noreferrer">Github</a></div></li>
    </ul>
  </>

export default CreditTab
