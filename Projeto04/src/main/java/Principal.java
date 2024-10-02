import corejava.Console;
import java.util.ArrayList;

public class Principal {

    public static void main(String[] args){

        final int TAM = Console.readInt("Informe o tamanho da lista: ");
        ListaDeObejetos listaDeEmpregados = new ListaDeObejetos(TAM);

        String nome;
        double salario;
        Empregado umEmpregado;
        int ID;
        boolean continua = true;
        while(continua){
            System.out.println('\n' + "O que você deseja fazer?:");
            System.out.println('\n' + "1. Cadastrar um empregado.");
            System.out.println('\n' + "2. Alterar empregado");
            System.out.println('\n' + "3. Remover empregado");
            System.out.println('\n' + "4. Consultar empregado");
            System.out.println('\n' + "5. Consultar com ID");

            int opcao = Console.readInt("Digite um número de 1 a 5: ");

            switch (opcao){
                case 0:
                    continua = false;
                    break;
                case 1:
                    nome = Console.readLine("Insira o nome: ");
                    salario = Console.readDouble("Insira o salário: ");

                    umEmpregado = new Empregado(nome, salario);
                    listaDeEmpregados.add(umEmpregado);

                    System.out.println("Empregado: " + '\n' + umEmpregado + '\n'+"Cadastrado!");
                    break;
                case 2:
                    ID = Console.readInt("Informe o ID do empregado: ");
                    boolean achou = false;
                    for (Empregado empregado : listaDeEmpregados) {
                        if (empregado.getID() == ID) {
                            achou = true;
                        }
                        if (achou) {
                            empregado.setNome(Console.readLine("Atualize o Nome: "));
                            empregado.setSalario(Console.readDouble("Atualize o Salário: "));
                            break;
                        }
                    }
                    if(!achou){
                        System.out.println("Este empregado não existe!");
                    }
                    break;
                case 3:
                    ID = Console.readInt("Informe o ID do empregado: ");
                    for(int i = 0; i < listaDeEmpregados.size(); i++){
                        umEmpregado = listaDeEmpregados.get(i);
                        if(umEmpregado.getID() == ID){
                            listaDeEmpregados.remove(i);
                            break;
                        }
                    }

                    break;
                case 4:
                    if(listaDeEmpregados.isEmpty()){
                        System.out.println('\n' + "Não há empregados na lista.");
                        break;
                    }
                    for (Empregado empregado : listaDeEmpregados) {
                        System.out.println(empregado);
                    }
                    break;
                case 5:
                    if(listaDeEmpregados.isEmpty()){
                        System.out.println('\n' + "Não há empregados na lista.");
                        break;
                    }
                    for (Empregado empregado : listaDeEmpregados) {
                        System.out.println("ID = " + empregado.getID() + " Nome = " + empregado.getNome());
                    }
                    break;
            }
        }
    }
}
