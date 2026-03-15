
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public int cadastrarProduto (ProdutosDTO produto)
    {                
        conn = new conectaDAO().connectDB();
        
        int statu;
        
        try
        {
            prep = conn.prepareStatement("INSERT INTO produtos (nome, valor, status) VALUES(?,?,?)");
            prep.setString(1,produto.getNome());
            prep.setInt(2,produto.getValor());
            prep.setString(3,produto.getStatus());
            
            statu = prep.executeUpdate();
            return statu;
        }
        catch(SQLException ex)
        {
            System.out.println("ERRO AO CONECTAR: " + ex.getMessage());
            return ex.getErrorCode();
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos()
    {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        String sql = "Select * from produtos";
        
        try(Connection conn = new conectaDAO().connectDB(); PreparedStatement prep = conn.prepareStatement(sql); ResultSet resultset = prep.executeQuery())
        {
            while(resultset.next())
            {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));
                listagem.add(produto);
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Erro ao listar produtos: " + ex.getMessage());
        }
        
        return listagem;
    }
    
    public int venderProduto(int idProduto)
    {
        conn = new conectaDAO().connectDB();
        int resultado;
        
        try
        {
            String sql = "UPDATE produtos SET status = ? WHERE id = ?";
            prep = conn.prepareStatement(sql);
            prep.setString(1, "VENDIDO");
            prep.setInt(2, idProduto);
            
            resultado = prep.executeUpdate();
            return resultado;
        }
        catch(SQLException ex)
        {
            System.out.println("Erro ao vender produto: " + ex.getMessage());
            return ex.getErrorCode();
        }
        finally
        {
            try
            {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
                
            }
            catch(SQLException e)
            {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
}

