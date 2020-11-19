import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirFileVisitor extends SimpleFileVisitor<Path> {

  private final Path source;
  private final Path target;

  protected CopyDirFileVisitor(Path source, Path target) {
    this.source = source;
    this.target = target;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    Path newDir = target.resolve(source.relativize(dir));

    Files.copy(dir, newDir, REPLACE_EXISTING);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    Path newFile = target.resolve(source.relativize(file));

    Files.copy(file, newFile, REPLACE_EXISTING);
    return FileVisitResult.CONTINUE;
  }
}
