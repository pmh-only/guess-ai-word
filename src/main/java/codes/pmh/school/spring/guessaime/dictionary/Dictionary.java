package codes.pmh.school.spring.guessaime.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public abstract class Dictionary {
  private List<String> words = null;

  @Autowired
  private ResourceLoader resourceLoader;

  public String getRandomWord () throws IOException {
    if (words == null)
      readWordsFromResourceFile();

    return words.get(getRandomWordIndex());
  }

  private int getRandomWordIndex () {
    Random random = new Random();
    return random.nextInt(this.words.size());
  }

  public void readWordsFromResourceFile () throws IOException {
    Resource dictonaryResource = getDictonaryResource();
    this.words = readLinesFromResource(dictonaryResource);
  }

  private Resource getDictonaryResource () {
    return resourceLoader.getResource(getResourcePath());
  }

  abstract protected String getResourcePath ();

  private List<String> readLinesFromResource (Resource resource) throws IOException {
    return Files.readAllLines(resource.getFile().toPath());
  }
}
