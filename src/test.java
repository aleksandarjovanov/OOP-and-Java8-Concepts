
interface A{
    void getViki();
}


class Parent{
    public void getViki(){
        System.out.println("Parent");
    }

    public void getAce(){
        System.out.println("PARENTT ACE");
    }
}

class Child1 extends Parent implements A{

    @Override
    public void getViki() {
        System.out.println("Child");
    }
}



public class test {
    public static void main(String[] args) {

        A objInterface = new Child1();

        objInterface.getViki();

        Parent p = new Child1();

        p.getViki();
        p.getAce();
    }
}
