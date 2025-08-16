public class Cliente {
    private static int IDcounter = 0;

    private String nome;
    private String email;
    private String nascimento;
    private double renda;
    private int ID;

    public Cliente(String nome, String email, String nascimento, double renda){
        this.nome = nome;
        this.email = email;
        this.nascimento = nascimento;
        this.renda = renda;
        this.ID = Cliente.IDcounter++;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }
}
