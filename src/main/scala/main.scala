import cats.Applicative
import cats.data._
import cats.mtl.ApplicativeAsk
import cats.effect.IO
import cats.Monad
import cats.implicits._

final case class TenantId(id: String)
final case class RequestContext(headers: Map[String, String])
final case class TenantContext(tenantId: TenantId, context: RequestContext)

// https://typelevel.org/blog/2018/10/06/intro-to-mtl.html

object obj {

  def getTenantContext: IO[TenantContext] = ???

  def program[F[_]: Monad](implicit A: ApplicativeAsk[F, TenantId]): F[String] =
    for {
      c <- A.ask
    } yield c.id

  // Can we get this for free?
  implicit def i[F[_]: Applicative]: ApplicativeAsk[ReaderT[F, TenantContext, *], TenantId] = new ApplicativeAsk[ReaderT[F, TenantContext, *], TenantId] {
    override val applicative: Applicative[ReaderT[F, TenantContext, *]] = ReaderT.catsDataApplicativeForKleisli[F, TenantContext]
    override def ask: ReaderT[F, TenantContext, TenantId] =  ReaderT(tc => Applicative[F].pure(tc.tenantId))
    override def reader[A](f: TenantId => A): ReaderT[F, TenantContext, A] = ReaderT(tc => Applicative[F].pure(f(tc.tenantId)))
  }

  def materializedProgram = program[ReaderT[IO, TenantContext, *]]

  def main: IO[String] = getTenantContext.flatMap(materializedProgram.run)

}
