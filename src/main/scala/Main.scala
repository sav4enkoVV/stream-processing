import cats.effect.{ExitCode, IO, IOApp, Resource}
import fs2.{Stream, io, text}
import java.io.InputStream
import cats.effect.unsafe.implicits.global
import cats.instances.tailRec
import scala.annotation.tailrec

object Main {
    def main(args: Array[String]) = {
        def resourceIO(name: String): IO[InputStream] =
            IO { getClass.getResourceAsStream(name) }

        val someTextFile: Stream[IO, Byte] =
            fs2.io.readInputStream(
                resourceIO("text.txt"),
                chunkSize = 4096,
                closeAfterUse = true
            )

        def words: fs2.Pipe[IO, String, String] =
            in => in.flatMap {
                line => Stream.emits(line.split("\\W+"))
            }

        val someInputTextFile = someTextFile
          .through(text.utf8Decode)
          .through(text.lines)
          .through(words)

        def wc: IO[List[String]] =
            someInputTextFile.fold(Map.empty[String, Int]) { (count, word) =>
                count + (word -> (count.getOrElse(word, 0) + 1))
            }
              .map(_.foldLeft("") {
                  case (accumulator, (word, count)) =>
                      accumulator + println( s"$word = $count\n")
              })
              .compile
              .toList

        val result = wc.unsafeRunSync()
    }
}





