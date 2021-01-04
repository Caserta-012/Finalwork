package client.characters;

import client.characters.actions.Action;
import client.configuration.Configs;
import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import client.mapObject.BaseObject;

public class Creature extends BaseObject implements Action {
    public enum CAMP {
        CALABASH,
        EVIL,
    }
    private Configs configs = Configs.getInstance();
    private StringProperty name = new SimpleStringProperty("Default");
    private IntegerProperty max_hp = new SimpleIntegerProperty(100);//最大生命值
    private IntegerProperty hp = new SimpleIntegerProperty(100); //当前生命值
    private IntegerProperty damage = new SimpleIntegerProperty(10); //攻击力，影响攻击强度
    private IntegerProperty defence = new SimpleIntegerProperty(5); //守备
    private IntegerProperty movement = new SimpleIntegerProperty(3); // 可移动的距离
    private IntegerProperty attackRange = new SimpleIntegerProperty(1); // 攻击距离
    private IntegerProperty abilityRange = new SimpleIntegerProperty(1);// 技能距离
    private Image image;  // 保存贴图
    private CAMP camp;    // 阵营
    private BooleanProperty attacked = new SimpleBooleanProperty(false); // 是否被攻击
    private IntegerProperty flash = new SimpleIntegerProperty(0);// 被攻击时闪烁
    private BooleanProperty dying = new SimpleBooleanProperty(false); //正在死亡
    private BooleanProperty canAct = new SimpleBooleanProperty(true);       // 是否能够行动
    private BooleanProperty canMove = new SimpleBooleanProperty(true);      // 是否能进行移动
    private BooleanProperty canAttack = new SimpleBooleanProperty(true);    // 是否能进行攻击
    private BooleanProperty canUseAbility = new SimpleBooleanProperty(true);// 是否能够使用技能

    public void setLocation(double x,double y){
        setX(x);
        setY(y);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getMax_hp() {
        return max_hp.get();
    }

    public IntegerProperty max_hpProperty() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.setHp(max_hp);
        this.max_hp.set(max_hp);
    }

    public int getHp() {
        return hp.get();
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp.set(hp);
    }


    public int getDamage() {
        return damage.get();
    }

    public IntegerProperty damageProperty() {
        return damage;
    }

    public void setDamage(int strength) {
        this.damage.set(strength);
    }

    public int getDefence() {
        return defence.get();
    }

    public IntegerProperty defenceProperty() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence.set(defence);
    }

    public int getMovement() {
        return movement.get();
    }

    public IntegerProperty movementProperty() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement.set(movement);
    }

    public int getAttackRange() {
        return attackRange.get();
    }

    public IntegerProperty attackRangeProperty() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange.set(attackRange);
    }

    public int getAbilityRange() {
        return abilityRange.get();
    }

    public IntegerProperty abilityRangeProperty() {
        return abilityRange;
    }

    public void setAbilityRange(int abilityRange) {
        this.abilityRange.set(abilityRange);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        this.setWidth(configs.getTILE_WIDTH());
        this.setHeight(configs.getTILE_HEIGHT());
    }

    public CAMP getCamp() {
        return camp;
    }

    public void setCamp(CAMP camp) {
        this.camp = camp;
    }

    public boolean getCanAct() {
        return canAct.get();
    }

    public BooleanProperty canActProperty() {
        return canAct;
    }

    public void setCanAct(boolean canAct) {
        this.canAct.set(canAct);
    }

    public boolean getCanMove() {
        return canMove.get();
    }

    public BooleanProperty canMoveProperty() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove.set(canMove);
    }


    public boolean getCanAttack() {
        return canAttack.get();
    }

    public BooleanProperty canAttackProperty() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack.set(canAttack);
    }

    public boolean getCanUseAbility() {
        return canUseAbility.get();
    }

    public BooleanProperty canUseAbilityProperty() {
        return canUseAbility;
    }

    public void setCanUseAbility(boolean canUseAbility) {
        this.canUseAbility.set(canUseAbility);
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        if (image != null){
            if(getCanAct()) //行动未结束
            {
                gc.setGlobalAlpha(1.0f);
            }
            else { //行动结束 设置透明度为0.5
                gc.setGlobalAlpha(0.5f);
            }
            if (getAttacked()){
                if (getFlash() < configs.getFLASH_NUM()){
                    if (getFlash() % 2 == 0)gc.setGlobalAlpha(0.0f);
                    else gc.setGlobalAlpha(1.0f);
                    setFlash(getFlash() + 1);
                }
                else {
                    setAttacked(false);
                }
            }
            if (isDying()){
                if (getFlash() < configs.getFLASH_NUM()){
                    gc.setGlobalAlpha(1.0f - getFlash() * 0.1f);
                    setFlash(getFlash() + 1);
                }
                else {
                    setDying(false);
                }
            }
            gc.drawImage(image,0,0,image.getWidth(),image.getHeight(),getX() + 10,getY() + 5,getWidth() - 20,getHeight() - 10);
            gc.setGlobalAlpha(1.5f);
        }
        gc.restore();
    }


    public void update() {

    }

    public void attack(Creature c) {
        c.setFlash(0);
        c.setAttacked(true);
        c.setHp(Math.max(Math.max(c.getHp() - (this.getDamage() - c.getDefence()),0), 0 ));
    }

    public void die() {
        setDying(true);
        setFlash(0);
    }

    public void moveTo(double dx, double dy) {

    }

    @Override
    public void moveX(double dx) {
        this.setX(getX() + dx);
    }

    @Override
    public void moveY(double dy) {
        this.setY(getY() + dy);
    }

    @Override
    public void useAbility(Creature c) {
        // 使用技能：对敌方造成2倍伤害(未计算防御前），后续将会对不同角色设计个性化技能
        c.setFlash(0);
        c.setAttacked(true);
        c.setHp(Math.max(Math.max(c.getHp() - (this.getDamage() * 2 - c.getDefence()),0), 0 ));
    }

    public boolean getAttacked() {
        return attacked.get();
    }

    public BooleanProperty attackedProperty() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked.set(attacked);
    }

    public int getFlash() {
        return flash.get();
    }

    public IntegerProperty flashProperty() {
        return flash;
    }

    public void setFlash(int flash) {
        this.flash.set(flash);
    }

    public boolean isCanAct() {
        return canAct.get();
    }

    public boolean isCanMove() {
        return canMove.get();
    }

    public boolean isCanAttack() {
        return canAttack.get();
    }

    public boolean isCanUseAbility() {
        return canUseAbility.get();
    }

    public boolean isDying() {
        return dying.get();
    }

    public BooleanProperty dyingProperty() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying.set(dying);
    }

    public void moveVX(int VX){
        this.moveX(VX * configs.getTILE_WIDTH());
    }

    public void moveVY(int VY){
        this.moveY(VY * configs.getTILE_HEIGHT());
    }

    public void reset(){
        setCanAct(true);
        setCanMove(true);
        setCanAttack(true);
    }

}
