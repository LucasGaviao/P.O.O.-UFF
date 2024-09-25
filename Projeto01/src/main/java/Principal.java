public class Principal {

    public static void main(String[] args){
        System.out.println("Olá Mundo!");
        Empregado e1 = new Empregado("Lucas", 5000);
        Empregado e2 = new Empregado("Ana", 7000);

        e1.setSalario(8000);
        System.out.println("E1 Nome: " + e1.getNome());
        System.out.println("E2 Nome: " + e2.getNome());

        System.out.println("telefone = " + e1.getTelefone());

        System.out.println(e1);

    }
}
