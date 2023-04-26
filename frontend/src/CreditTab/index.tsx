import { type FC } from 'react'
import style from './style.module.scss'
import { MdHeartBroken, MdMail } from 'react-icons/md'
import TitleBar from '../TitleBar'

const CreditTab: FC = () =>
  <>
    <TitleBar isFreepass title="게임 정보" />
    <section className={style.credit}>
      <h1>Credit.</h1>
      <p>Guess AI Word</p>

      <h2>Spring Backend</h2>
      <p>Minhyeok Park</p>

      <h2>React.js Frontend</h2>
      <p>Minhyeok Park</p>

      <h2>Powered by</h2>
      <p>OpenAI GPT3.5 API</p>

      <h2>Design ref. by</h2>
      <p>Google Material Design 3</p>

      <h2>Source code</h2>
      <p><a target='_blank' href="https://github.com/pmh-only/guess-ai-word" rel="noreferrer">pmh-only/guess-ai-word</a></p>

      <h2>Copyright</h2>
      <p>Copyright &copy; 2023. Minhyeok Park.</p>

      <p><MdMail /> opensource@pmh.codes</p>
      <p><MdHeartBroken /></p>
    </section>
  </>

export default CreditTab
