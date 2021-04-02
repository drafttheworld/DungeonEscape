/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Andrew
 */
public class GameInstructions {

    public JScrollPane buildGameInstructions() {

        JScrollPane jScrollPane = new JScrollPane();
        String gameInstructions = getGameInstructions();
        JTextArea jTextArea = new JTextArea(gameInstructions);

        jTextArea.setMinimumSize(new Dimension(300, 300));
        jTextArea.setMaximumSize(new Dimension(300, 300));
        jTextArea.setPreferredSize(new Dimension(300, 300));

        jTextArea.setLineWrap(true);
        jTextArea.setTabSize(4);
        jScrollPane.add(jTextArea);

        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        jScrollPane.setMinimumSize(new Dimension(300, 300));
        jScrollPane.setMaximumSize(new Dimension(300, 300));
        jScrollPane.setPreferredSize(new Dimension(300, 300));

        return jScrollPane;
    }

    private String getGameInstructions() {

        StringBuilder sb = new StringBuilder("GAMEPLAY INSTRUCTIONS\n\n");

        return sb.append(getGameplayDescription())
            .append(getCharacterDescriptions())
            .append(getMineDescriptions())
            .append(getItemDescriptions())
            .append(getPowerUpDescriptions())
            .toString();
    }

    private String getGameplayDescription() {

        StringBuilder sb = new StringBuilder();

        sb.append("Starting a new game:\n\n")
            .append("\tAfter clicking the \"Start New Game\" button the player should use the drop down menu to choose "
                + "the dungeon size. The dungeon size will determine the size of the map along with the number of "
                + "items and enemies in play. Keep in mind that larger maps will also increase the potential score "
                + "that is achievable.\n\n")
            .append("The dungeon map:\n\n")
            .append("\tThe map will display the tiles of the map that have been exposed to the player based on the "
                + "player's line of sight. Additional map tiles are revealed to the player as the map is explored, "
                + "tiles that have been revealed remain visible to the player throughout gameplay. Enemies will only "
                + "be visible to the player with the player's current \"line-of-sight\" distance.\n\n")
            .append("Player movement:\n\n")
            .append("\tPlayer movement is performed using either the a(left) s(down) d(right) w(up) or arrow keys on "
                + "the keyboard.\n\n")
            .append("How to play:\n\n")
            .append("\tThe goal of this game is to make it out of the dungeon with the most points. The dungeon will "
                + "have a number of exits based on the difficulty, the higher the difficulty the fewer the exits. Make "
                + "it to the exit and the game is over. If the Dungeon Master attacks you before you make it to an "
                + "exit, you lose the game. The game is turn based, you move one space per turn and then the other "
                + "characters move. Enemies have two modes of movement, patrolling and hunting. While patrolling "
                + "enemies move in random directions and when they move to a space within detection distance of the "
                + "player, they switch to hunting mode which results in the enemy tracking the player until either the "
                + "player teleports away from the enemy or the enemy catches the player. Points are accumulated by "
                + "collecting coins with a bonus given to coins remaining in inventory when exiting the dungeon. "
                + "Additional points are awarded based on the number of turns taken in relation to the number of "
                + "occupiable spaces on the board, specifically the fewer turns used to escape the dungeon the more "
                + "points awarded.\n\n");

        return sb.toString();
    }

    private String getCharacterDescriptions() {

        StringBuilder sb = new StringBuilder("Characters\n\n");

        sb.append("\tKnight:\n\n")
            .append("\t\tThis is you. You play as a captured knight who must escape the dungeon and it's creatures to "
                + "win the day and find any chance of making it home. The knight may only move one space per turn.")
            .append("\tGhost:\n\n")
            .append("\t\tGhosts are nasty little buggers which have the farthest detection distance of all enemies and "
                + "are terrifying to encounter but fortunately cannot cause any permanent damage.\n\n")
            .append("\t\t\tMovement: Ghosts have the largest freedom of movement. Typically, they will be able to "
                + "move several spaces while patrolling and then usually one or two spaces once they are hunting. "
                + "Unlike other characters, ghosts can move through walls.\n")
            .append("\t\t\tAttack: Freezes the player for an extended amount time (10-20 turns).\n\n")
            .append("Guard:\n\n")
            .append("\t\tGuards are the cursed skeletal remanent of the former dungeon staff. They have one purpose "
                + "in their afterlife which is to keep their dungeon residents in their cells.\n\n")
            .append("\t\t\tMovement: Guards patrol the dungeon usually a few spaces at a time however once they "
                + "detect a loose captive, they become much more focused and as a result slow down a little so that "
                + "they can track their target.\n")
            .append("\t\t\tAttack: When a guard captures you, they drag you back to your cell at the center of the "
                + "dungeon where you will have to start your escape all over again. The guard will respawn randomly "
                + "to another part of the map after capturing its prisoner.\n\n")
            .append("Dungeon Master:\n\n")
            .append("\t\tThe Dungeon Master is the biggest, baddest enemy that you will encounter. If caught by a "
                + "Dungeon Master, the game is over, so avoid at all costs!")
            .append("\t\t\tMovement: Dungeon Masters spawn at the center of the dungeon after a set number of turns "
                + "(all spawn at the same time) and then start searching for you. They usually move a few spaces at a "
                + "time while patrolling and then slow down to 2 spaces per turn once they detect you. They are faster "
                + "than you so if they find you, head for either the exit or the nearest teleport mine, whichever is "
                + "closest.\n")
            .append("\t\t\tAttack: Dungeon Masters show no mercy or forgiveness to prisoners who escape their cells, "
                + "they will crush you...gave over.\n");

        return sb.toString();
    }

    private String getMineDescriptions() {

        StringBuilder sb = new StringBuilder("Mines\n\n");

        sb.append("\tFreeze mines: Freeze mines freeze the player in place for random number of turns.\n\n")
            .append("\tTeleport mines: Teleport mines teleport the user to a random location in the dungeon. These may "
                + "either work for or against the player depending on the situation, if trying to escape an attacking "
                + "enemy these can be a good way to get away.\n\n");

        return sb.toString();
    }

    private String getItemDescriptions() {

        StringBuilder sb = new StringBuilder("Items\n\n");

        sb.append("\tCoins: Coins are way to increase your score. The player is awarded 1 point for every coin "
            + "collected and those still in inventory at the end of the game are given a multiplier. It is therefore "
            + "important to keep in mind that if you are captured by a guard then all your coins in inventory are "
            + "lost.\n\n")
            .append("\tPower-up Boxes: Power-up boxes award a random power-up to the player. Power-ups in the player's "
                + "inventory may be activated as needed and last a set number of turns. Once a power-up is used it is "
                + "then removed from the player's inventory. Like coins, if the player is captured by a guard then all "
                + "of power-ups in inventory are lost.\n\n");

        return sb.toString();
    }

    private String getPowerUpDescriptions() {

        StringBuilder sb = new StringBuilder("Power-Ups\n\n");

        sb.append("\tInvincibility: Renders the player immune to effects of attack by enemies. Enemies which attack "
            + "while you are invincible will continue to follow and attack so it is important to reach safety before "
            + "the power-up is expended.\n\n")
            .append("\tInvisibility: Prevents enemies from being able to detect the player. Enemies will continue "
                + "their normal searching pattern but cannot detect or attack the player while invisible.\n\n")
            .append("\tRepellent: Causes enemies who come into detection range of the player to be repelled from the "
                + "player. Enemies will not be able to get close enough to attack the player while repellent is "
                + "active.\n\n")
            .append("\tTerminator: The Terminator power-up causes the player to permanently remove attacking enemies "
                + "from the dungeon.");

        return sb.toString();
    }
}
