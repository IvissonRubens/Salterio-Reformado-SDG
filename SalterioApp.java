import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe principal do Saltério Reformado.
 * Mostra uma grade com 150 salmos e permite abrir cada salmo em janela
 * separada.
 * Textos dos salmos são carregados da classe Salmos.java através de método
 * estático.
 */
public class SalterioApp extends JFrame {

    // Mapa que armazena os salmos: chave = número do salmo, valor = mapa de modos
    // (A, B, etc.)
    private Map<Integer, Map<String, String>> salmos;

    // Conjunto de salmos indisponíveis (desativados na interface)
    private Set<Integer> salmosIndisponiveis;

    public SalterioApp() {
        setTitle("Saltério Reformado");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Carrega todos os salmos do outro arquivo
        salmos = Salmos.getSalmos();

        // Inicializa salmos indisponíveis
        salmosIndisponiveis = new HashSet<>();
        carregarSalmosIndisponiveis();

        // Cria painel de botões dos salmos
        JPanel panelSalmos = new JPanel(new GridLayout(10, 15, 5, 5));

        for (int i = 1; i <= 150; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setPreferredSize(new Dimension(50, 50));
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));

            if (salmosIndisponiveis.contains(i)) {
                // Salmo indisponível
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(Color.DARK_GRAY);
                btn.setEnabled(false);
            } else {
                // Salmo disponível
                btn.setBackground(new Color(0, 150, 136));
                btn.setForeground(Color.WHITE);
                final int salmoNumero = i;
                btn.addActionListener(e -> exibirSalmo(salmoNumero));
            }
            panelSalmos.add(btn);
        }

        add(panelSalmos, BorderLayout.CENTER);
    }

    /**
     * Lista de salmos indisponíveis
     */
    private void carregarSalmosIndisponiveis() {
        int[] indisponiveis = {
                18, 35, 41, 44, 45, 49, 52, 56, 58, 59, 60, 68, 69, 71, 74, 75,
                76, 77, 79, 80, 82, 83, 86, 89, 92, 96, 102, 104, 105, 106, 107,
                108, 109, 111, 136, 141, 145, 147
        };

        for (int num : indisponiveis) {
            salmosIndisponiveis.add(num);
        }
    }

    /**
     * Exibe o salmo em uma nova janela
     * 
     * @param numero número do salmo
     */
    private void exibirSalmo(int numero) {
        Map<String, String> modos = salmos.get(numero);
        if (modos == null) {
            JOptionPane.showMessageDialog(this, "Cântico não disponível.", "Salmo " + numero,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFrame salmoFrame = new JFrame("Salmo " + numero);
        //salmoFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        salmoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        salmoFrame.setSize(800, 1000);
        salmoFrame.setLocationRelativeTo(this);
        //salmoFrame.setLocation(1, 1); //para começar no começo do salmos
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 25));
        areaTexto.setEditable(false);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        salmoFrame.add(scrollPane, BorderLayout.CENTER);

        // Seleção de modos (A, B, etc.)
        String[] modosDisponiveis = modos.keySet().toArray(new String[0]);
        if (modosDisponiveis.length > 1) {
            String modoSelecionado = (String) JOptionPane.showInputDialog(
                    salmoFrame, "Escolha o modo do Salmo " + numero + ":",
                    "Selecionar Modo", JOptionPane.QUESTION_MESSAGE,
                    null, modosDisponiveis, modosDisponiveis[0]);

            if (modoSelecionado != null) {
                areaTexto.setText(modos.get(modoSelecionado));
            } else {
                // Se clicou em cancelar, fecha a janela
                salmoFrame.dispose();
                return;
            }
        } else {
            areaTexto.setText(modos.get(modosDisponiveis[0]));
        }

        // Botões de aumentar/diminuir fonte
        JPanel panelBotoes = new JPanel();
        JButton btnAumentar = new JButton("Aumentar Fonte");
        btnAumentar.addActionListener(e -> {
            Font font = areaTexto.getFont();
            areaTexto.setFont(font.deriveFont(font.getSize() + 2f));
        });

        JButton btnDiminuir = new JButton("Diminuir Fonte");
        btnDiminuir.addActionListener(e -> {
            Font font = areaTexto.getFont();
            areaTexto.setFont(font.deriveFont(font.getSize() - 2f));
        });

        panelBotoes.add(btnAumentar);
        panelBotoes.add(btnDiminuir);
        salmoFrame.add(panelBotoes, BorderLayout.SOUTH);

        salmoFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalterioApp().setVisible(true));
    }
}