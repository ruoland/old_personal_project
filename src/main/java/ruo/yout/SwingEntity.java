package ruo.yout;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import olib.api.WorldAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingEntity extends JFrame implements ActionListener {
    JTextField spawnCount1 = new JTextField(4), spawnCount2 = new JTextField(4);
    JButton entityLockButton = new JButton("엔티티 잠금");
    JButton entityUnlockButton = new JButton("엔티티 잠금 해제");
    JButton entitySpawnButton = new JButton("소환");
    JPanel attackPanel = new JPanel();
    JButton entityAttackButton = new JButton("공격");
    JButton entityUnAttackButton = new JButton("공격해제");
    JButton entitySpawnLockButton = new JButton("모두 스폰 락 : false");
    JPanel blockPanel = new JPanel();
    JButton removeBlockButton = new JButton("블럭 제거");
    JButton setBlockButton = new JButton("블럭 설치");

    static JCheckBox skelDelay0Box = new JCheckBox("스켈레톤 딜레이");
    static JCheckBox uiOffBox = new JCheckBox("UI 끄기");
    static JCheckBox healthBox = new JCheckBox("체력 표시하기");

    JComboBox<String> entityComboBox = new JComboBox<String>();
    JComboBox<String> entitySecondComboBox = new JComboBox<String>();

    public SwingEntity() {
        setTitle("모제");
        setSize(450, 300);
        setLocation(0, 0);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        setEntityComboBox(entityComboBox);
        setEntityComboBox(entitySecondComboBox);
        entitySpawnButton.addActionListener(this);
        entityComboBox.addActionListener(this);
        entitySecondComboBox.addActionListener(this);
        entityLockButton.addActionListener(this);
        entityUnlockButton.addActionListener(this);
        entityAttackButton.addActionListener(this);
        entitySpawnLockButton.addActionListener(this);
        entityUnAttackButton.addActionListener(this);
        removeBlockButton.addActionListener(this);
        setBlockButton.addActionListener(this);
        skelDelay0Box.addActionListener(new SwingSkelDelay0Listener());
        uiOffBox.addActionListener(new SwingSkelDelay0Listener());
        healthBox.addActionListener(new SwingSkelDelay0Listener());

        add(entityComboBox);
        add(entitySecondComboBox);
        add(spawnCount1);
        add(spawnCount2);
        add(entitySpawnButton);
        add(entityLockButton);
        add(entityUnlockButton);
        attackPanel.add(entityAttackButton);
        attackPanel.add(entityUnAttackButton);
        add(attackPanel);
        add(entitySpawnLockButton);
        blockPanel.add(setBlockButton);
        blockPanel.add(removeBlockButton);
        add(blockPanel);
        add(skelDelay0Box);
        add(uiOffBox);
        add(healthBox);
        setVisible(true);
    }

    public void setEntityComboBox(JComboBox box) {
        box.addItem("Zombie");
        box.addItem("Creeper");
        box.addItem("Skeleton");
        box.addItem("Spider");
        box.addItem("Enderman");
        box.addItem("WitherBoss");
        box.addItem("Enderdragon");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == entitySpawnButton) {
            boolean lockEntity = true;
            String selectBox1 = (String) entityComboBox.getSelectedItem();
            String selectBox2 = (String) entitySecondComboBox.getSelectedItem();
            if (!Mojae.monterAttack.containsKey(selectBox1)) {
                int result = JOptionPane.showConfirmDialog(this, "소환하기 전에 공격 AI를 추가 할까요?.", "AI", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    MojaeTest.attack(selectBox1, selectBox2);
                }
            }
            if (!Mojae.noTeamKill) {
                int teamKill = JOptionPane.showConfirmDialog(this, "팀킬 방지 기능을 킬까요?", "AI", JOptionPane.YES_NO_OPTION);
                if (teamKill == JOptionPane.YES_OPTION) {
                    MojaeTest.teamKill(true);
                }
            }
            int lock = JOptionPane.showConfirmDialog(this, "잠금 해제 할까요?", "AI", JOptionPane.YES_NO_OPTION);
            if (lock == JOptionPane.YES_OPTION) {
                lockEntity = false;
            }
            int block = JOptionPane.showConfirmDialog(this, "블럭 없앨까요?", "AI", JOptionPane.YES_NO_OPTION);
            if (!WorldAPI.getWorld().isAirBlock(new BlockPos(491, 5, 516))) {
                if (block == JOptionPane.YES_OPTION) {
                    MojaeTest.removeWall();
                }
            }
            if (MoJaeEvent.attackDelay == -1) {
                int attacDelay = JOptionPane.showConfirmDialog(this, "무적 시간 없앨까요?", "AI", JOptionPane.YES_NO_OPTION);
                if (attacDelay == JOptionPane.YES_OPTION) {
                    MojaeTest.attackDelay(true);
                }
            }

            if (selectBox1.equalsIgnoreCase("zombie") || selectBox2.equalsIgnoreCase("zombie")
                    || selectBox1.equalsIgnoreCase("skeleton") || selectBox2.equalsIgnoreCase("skeleton")) {
                if (!WorldAPI.getWorld().isDaytime()) {
                    int time = JOptionPane.showConfirmDialog(this, "밤으로 바꿀까요?", "AI", JOptionPane.YES_NO_OPTION);
                    if (time == JOptionPane.YES_OPTION) {
                        WorldAPI.command("time set night");
                    }
                }
            }
            if (!Mojae.wither && selectBox1.equalsIgnoreCase("WitherBoss") || selectBox2.equalsIgnoreCase("WitherBoss")) {
                int time = JOptionPane.showConfirmDialog(this, "스켈레톤이 위더를 잡을 수 있게 할까요?", "AI", JOptionPane.YES_NO_OPTION);
                if (time == JOptionPane.YES_OPTION) {
                    Mojae.wither = true;
                }
            }
            MojaeTest.monsterSpawn(selectBox1, selectBox2, Integer.valueOf(spawnCount1.getText()), Integer.valueOf(spawnCount2.getText()), lockEntity);
        }

        if (e.getSource() == entityLockButton) {
            MoJaeEvent.lockList.add((String) entityComboBox.getSelectedItem());
        }
        if (e.getSource() == entityUnlockButton) {
            WorldAPI.command("unlockentity " + entityComboBox.getSelectedItem());
            WorldAPI.command("unlockentity " + entitySecondComboBox.getSelectedItem());
            int result = JOptionPane.showConfirmDialog(this, "블럭도 없앨까요?.", "AI", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                WorldAPI.setBlock(WorldAPI.getWorld(), 18, 5, 15, 2, 4, 15, Blocks.AIR);
                WorldAPI.setBlock(WorldAPI.getWorld(), 18, 5, 5, 2, 4, 5, Blocks.AIR);
            }
        }
        if (e.getSource() == entityAttackButton) {
            Mojae.monterAttack.put((String) entityComboBox.getSelectedItem(), (String) entitySecondComboBox.getSelectedItem());
            Mojae.monterAttack.put((String) entitySecondComboBox.getSelectedItem(), (String) entityComboBox.getSelectedItem());

        }
        if (e.getSource() == entitySpawnLockButton) {
            JButton button = (JButton) e.getSource();
            Mojae.spawnLockMode = !Mojae.spawnLockMode;
            button.setText("모두 스폰 락:" + Mojae.spawnLockMode);
        }
        if (e.getSource() == entityUnAttackButton) {
            Mojae.monterAttack.remove((String) entityComboBox.getSelectedItem());
            Mojae.monterAttack.remove((String) entitySecondComboBox.getSelectedItem());
        }
        if (e.getSource() == setBlockButton) {
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 5, 15, 2, 4, 15, Blocks.PURPUR_BLOCK);
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 5, 5, 2, 4, 5, Blocks.PURPUR_BLOCK);
        }
        if (e.getSource() == removeBlockButton) {
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 5, 15, 2, 4, 15, Blocks.AIR);
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 5, 5, 2, 4, 5, Blocks.AIR);
        }
        if (e.getSource() == entityUnAttackButton) {
            Mojae.monterAttack.remove((String) entityComboBox.getSelectedItem());
            Mojae.monterAttack.remove((String) entitySecondComboBox.getSelectedItem());
        }

    }
}
