package checkURLTest;

import edu.java.bot.model.url_utils.CheckURL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckURLTest {
    @Test
    @DisplayName("CorrectURL")
    void testCorrectUrl() {
        String checkURL =
            CheckURL.checkURL("https://github.com/clockbel/Java-Tinkoff");
        assertThat(checkURL).isEqualTo("OK");
    }
    @Test
    @DisplayName("IncorrectURL")
    void testCorrectUr2() {
        String checkURL =
            CheckURL.checkURL("https://github.com/clockbel/Java-Tinko");
        assertThat(checkURL).isEqualTo("BAD URL");
    }
}
