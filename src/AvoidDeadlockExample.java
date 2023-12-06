import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AvoidDeadlockExample {

    public static class MyClass1 {
        private final Lock lock = new ReentrantLock();

        public void method1(MyClass2 myClass2) {
            lock.lock();
            try {
                System.out.println("MyClass1 method1");
                Thread.sleep(1000);
                myClass2.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void method2() {
            lock.lock();
            try {
                System.out.println("MyClass1 method2");
                // Some work
            } finally {
                lock.unlock();
            }
        }
    }

    public static class MyClass2 {
        private final Lock lock = new ReentrantLock();

        public void method1(MyClass1 myClass1) {
            lock.lock();
            try {
                System.out.println("MyClass2 method1");
                Thread.sleep(1000);
                myClass1.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void method2() {
            lock.lock();
            try {
                System.out.println("MyClass2 method2");
                // Some work
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        MyClass1 myClass1 = new MyClass1();
        MyClass2 myClass2 = new MyClass2();

        Thread thread1 = new Thread(() -> myClass1.method1(myClass2));
        Thread thread2 = new Thread(() -> myClass2.method1(myClass1));

        thread1.start();
        thread2.start();
    }
}
