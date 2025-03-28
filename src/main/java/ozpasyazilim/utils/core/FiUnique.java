package ozpasyazilim.utils.core;

public class FiUnique {


    /**
     * Unix zaman damgasını (milisaniye tabanlı) kullanarak kısa bir benzersiz değer üretebilirsiniz.
     * Zaman damgasını başka bir sayıyla birleştirerek çakışmaları azaltabilirsiniz.
     * Eğer dağıtık bir sistem için benzersizlik sağlamak gerekirse,
     * IP gibi diğer bilgilere de başvurabilirsiniz.
     *
     * @return
     */
    public static String generateTimeBasedId() {
        long timeStamp = System.currentTimeMillis();
        int randomNum = (int) (Math.random() * 1000); // 0-999 arasında rastgele sayı
        return Long.toString(timeStamp, 36) + Integer.toString(randomNum, 36);
    }

    public static void main(String[] args) {
        System.out.println(generateTimeBasedId());
    }

}
