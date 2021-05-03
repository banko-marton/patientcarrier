import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.*;

public class HospitalView extends GridWorldView {
    private HospitalEnvironment env;

    public HospitalView(GridWorldModel model) {
        super(model, "Hospital Logistics", 600);
        this.model = (HospitalModel) model;
        setVisible(true);
        repaint();
    }

    private JPanel updateCapacities(){
        JPanel panel = new JPanel();
        HashMap<Department, Location> asd = env.getDepartments();
        if(asd == null) return panel;
        Set<Department> departments = asd.keySet();

        for (Department d : departments){
            panel.add(new JLabel(d.getDepartmentType().toString() + ": " + d.getCurrentCapacity()));
        }
        return panel;
    }

    private JPanel updateStatus(){
        JPanel panel = new JPanel();
        ArrayList<Carrier> carriers = env.getCarrierAgents();
        for(Carrier c : carriers){
            String s = "Carrier #" + c.getId() + " is currently";
            if(c.getTakenId() != -1L){
                s += " carrying Patient#" + c.getTakenId();
            }
            else{
                s += " waiting";
            }
            panel.add(new JLabel(s));
        }
        return panel;
    }


    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        JComboBox illnessTypes = new JComboBox();
        for (SicknessType type : SicknessType.values()) {
            illnessTypes.addItem(type.name());
        }

        JPanel sp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sp.setBorder(BorderFactory.createEtchedBorder());
        sp.add(new JLabel("Add new Patient:"));
        sp.add(new JLabel("Choose the illness type"));
        sp.add(illnessTypes);
        JButton addPatient = new JButton();
        sp.add(addPatient);

        sp.add(new JLabel("Department capacities:"));
        //JPanel capacities = updateCapacities();
        //sp.add(capacities);

        sp.add(new JLabel("Status of Carrier Agents:"));
        //JPanel agentStatus = updateStatus();
        //sp.add(agentStatus);

        JPanel p = new JPanel();

        JPanel s = new JPanel(new BorderLayout());
        s.add(BorderLayout.WEST, sp);
        s.add(BorderLayout.EAST, p);
        getContentPane().add(BorderLayout.SOUTH, s);

        // Events handling
        addPatient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                env.addPatient(Enum.valueOf(SicknessType.class, (String) illnessTypes.getSelectedItem()));
            }
        } );


    }

    @Override
    public void draw(Graphics g, int x, int y, int object) {
        if(object >= 10){
            g.setColor(Color.ORANGE);
            g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.setColor(Color.WHITE);
            g.drawString(SicknessType.values()[object - 10].name().substring(0, 2), x * cellSizeW, y * cellSizeH);
        }
        else if(object == 1){
            g.setColor(Color.RED);
            g.fillRect(x * cellSizeW - 5*cellSizeW, y * cellSizeH, 10*cellSizeW, cellSizeH);
            g.setColor(Color.WHITE);
            g.drawString("R", x * cellSizeW, y * cellSizeH);
        }

        /*updating panels...*/
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        if (id < 0) return;
        g.setColor(Color.BLUE);
        g.drawOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        ArrayList<Carrier> ac = env.getCarrierAgents();
        System.out.println(id);
        Carrier carrier = ac.get(id);

        if (carrier.getTakenId() != -1L) {
            g.setColor(Color.RED);
            g.drawOval(x * cellSizeW + cellSizeW / 2, y * cellSizeH + cellSizeH / 2, cellSizeW / 2, cellSizeH / 2);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(carrier.getTakenId()), x * cellSizeW + cellSizeW / 2, y * cellSizeH + cellSizeH / 2);
        }
    }

    public static void main(String[] args) throws Exception {
        HospitalEnvironment env = new HospitalEnvironment(/*24, 5*/);
        System.out.println("ez szep esjo");
        env.init(new String[] {"24","1"});
    }

    public void setEnv(HospitalEnvironment e){
        this.env = e;
    }
}
