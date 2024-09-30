import corejava.Console;
import java.util.ArrayList;

public class Principal {

    public static void main(String[] args){

        final int TAM = Console.readInt("Informe o tamanho da lista: ");
        ArrayList listaDeEmpregados = new ArrayList(TAM);

        String nome;
        double salario;
        Empregado umEmpregado;

        boolean continua = true;
        while(continua){
            System.out.println('\n' + "O que você deseja fazer?:");
            System.out.println('\n' + "1. Cadastrar um empregado.");
            System.out.println('\n' + "2. Alterar empregado");
            System.out.println('\n' + "3. Remover empregado");
            System.out.println('\n' + "4. Consultar empregado");
            System.out.println('\n' + "5. ");

            int opcao = Console.readInt("Digite um número de 1 a 5: ");

            switch (opcao){
                case 1:
                    nome = Console.readLine("Insira o nome: ");
                    salario = Console.readDouble("Insira o salário: ");

                    umEmpregado = new Empregado(nome, salario);

                    listaDeEmpregados.add(umEmpregado);
                case 2:

                case 3:

                case 4:
                    for(int i = 0; i < listaDeEmpregados.size(); i++){
                        umEmpregado = (Empregado) listaDeEmpregados.get(i);
                        System.out.println(umEmpregado);
                    }
            }
        }
    }
}
