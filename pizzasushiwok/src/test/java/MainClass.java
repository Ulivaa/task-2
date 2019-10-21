import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;


public class MainClass {

    public ChromeDriver driver;
    public WebDriverWait wait;
    public String url;
    public JavascriptExecutor js;

    @Before
    public void setUp(){ // метод начинается, до загрузки теста и задает необходимые параметры
//        инициализация драйвера
        System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");
        driver=new ChromeDriver();
//        время явного ожидания в секундах
        wait = new WebDriverWait(driver, 3);
//        время неявного ожидания
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        url начальной страницы сайта для теста
        url="https://pizzasushiwok.ru/";
//        для работы с js
        js=(JavascriptExecutor) driver;
    }

    @Test
    public  void run() throws InterruptedException {
//        загрузка начальной страницы
        driver.get(url);
//        переход в меню на страницу "пиццы"
        driver.findElement(By.xpath("//nav//a//span[contains(text(),'Пиццы')]")).click();

//        Заказ от 600 рублей- выбор пиццы 30см
        driver.findElement(By.xpath("//div[@id='item852']//*[contains(text(),\'30см\')]")).click();
//        добавление в корзину пиццы "гудБиф"
        driver.findElement(By.xpath("//div[@id='item852']//a[contains(text(),\'в корзину\')]")).click();

//        ожидание сокрытия летающего элемента увеличения счетчика
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("flyCart")));
//        переход в корзину
        driver.findElement(By.xpath("//div//*[@class='cart to_cart']")).click();

//        Ожидание загрузки и удаление элемента чата-"Напишите нам, мы онлайн!", который перекрывает доступ к кнопке "далее"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jvlabelWrap")));
        WebElement chat=driver.findElement(By.id("jvlabelWrap"));
        js.executeScript("arguments[0].parentNode.removeChild(arguments[0])", chat);

//        нажатие на кнопку "далее"
        driver.findElement(By.cssSelector(".btn > span")).click();

//        ввод данных для оформления заказа
        driver.findElement(By.id("order_name")).sendKeys("Петр");
        WebElement phone=driver.findElement(By.id("order_phone"));
        String number="8-916-456-34-35";
//        цикл, необходимый для посимвольного правильного ввода номера, т.к. в строке автоматическое проставление символа "-"
        for (int i=0; i<number.length(); i++){
            TimeUnit.MILLISECONDS.sleep(100);
            phone.sendKeys(String.valueOf(number.charAt(i)));
        }
        driver.findElement(By.id("order_street")).sendKeys("Большая дмитровка");
        driver.findElement(By.id("order_home_user")).sendKeys("5");
        driver.findElement(By.xpath("//label[contains(text(),'Банковской картой на сайте')]")).click();
    }

    @After
    public void stop(){// выход драйвера. Выключено для удобства просмотра скрипта
//        driver.quit();
    }
}
