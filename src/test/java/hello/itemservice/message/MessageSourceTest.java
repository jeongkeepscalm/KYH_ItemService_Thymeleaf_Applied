package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@SpringBootTest
public class MessageSourceTest {

  @Autowired
  MessageSource ms;

  @Test
  void helloMessage() {
    /* locale 정보가 없으면 basename 에서 설정한 기본 이름 메시지 파일을 조회 */
    String result = ms.getMessage("hello", null, null);
    Assertions.assertThat(result).isEqualTo("안녕");
  }

  @Test
  void helloMessageArgs() {
    /* 매개변수 사용 */
    String result = ms.getMessage("hello.name", new Object[]{new String("오정길")}, null);
    Assertions.assertThat(result).isEqualTo("안녕 오정길");
  }

  @Test
  void notFoundMessageCode() {
    /* 메시지가 없는 경우 NoSuchMessageException 발생 */
    Assertions.assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
            .isInstanceOf(NoSuchMessageException.class);
  }

  @Test
  void notFoundMessageCodeDefaultMessage() {
    /* 지정된 메시지가 없어도 기본 메시지를 설정하여 활용 가능 */
    String result = ms.getMessage("no_code", null, "Default Message :::", null);
    Assertions.assertThat(result).isEqualTo("Default Message :::");
  }

  @Test
  void defaultLanguage() {
    /*
    * locale 정보가 없음
    *   -> 시스템 기본 locale이 ko_KR 이므로 messages_ko.properties 조회
    *   -> 조회 실패
    *   -> messages.properties
    * */
    Assertions.assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");

    /* locale 정보가 있지만, message_ko 가 없으므로 messages 를 사용 */
    Assertions.assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
  }

  @Test
  void EnglishLanguage() {
    Assertions.assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
  }

}
