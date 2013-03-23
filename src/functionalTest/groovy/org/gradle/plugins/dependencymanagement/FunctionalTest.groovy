package org.gradle.plugins.dependencymanagement

import org.junit.Test
import org.testng.Assert

/**
 */
class FunctionalTest {
  private File base = new File(System.getProperty('projects.basedir'))

  @Test(timeout = 10000L)
  def void multiProject() {
    Process test = new ProcessBuilder("gradle", "build").directory(new File(base, 'multi-project')).start()
    def exitCode  = test.waitFor()
    printAll(System.out, test.inputStream)
    printAll(System.err, test.errorStream)
    Assert.assertEquals(exitCode, 0)
  }

  private static void printAll(PrintStream out, InputStream stream) {
    def reader = new BufferedReader(new InputStreamReader(stream))
    String line = reader.readLine()
    while (null != line) {
      out.println(line)
      line = reader.readLine()
    }
    out.flush()
  }
}
