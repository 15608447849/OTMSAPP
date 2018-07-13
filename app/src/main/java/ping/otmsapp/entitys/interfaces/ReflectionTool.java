package ping.otmsapp.entitys.interfaces;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import ping.otmsapp.R;
import ping.otmsapp.entitys.annotations.Rid;
import ping.otmsapp.entitys.annotations.RidName;

/**
 * Created by lzp on 2018/3/1.
 * 反射工具
 */

public class ReflectionTool {

    private static int getResource(String name) throws Exception{
            Class idClass = R.id.class;
            Field field = idClass.getField(name);
            return  field.getInt(name);
    }

    private static void setViewValue(Object holder,String fieldName,View viewRoot) throws Exception{
           setViewValue(holder,fieldName,getResource(fieldName),viewRoot);
    }
    private static void setViewValue(Object holder,String fieldName,String RidName,View viewRoot) throws Exception{
        setViewValue(holder,fieldName,getResource(RidName),viewRoot);
    }
    private static void setViewValue(Object holder,String fieldName,int rid,View viewRoot) throws Exception{
        Field f = holder.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(holder,viewRoot.findViewById(rid));
    }

    private static void setViewHolderAbsValue(Object holder, String fieldName, Class creatClazz, Context context, View viewRoot) throws Exception{
        Constructor cons = creatClazz.getConstructor(Context.class,View.class);//获取有参构造
        Field f = holder.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(holder, cons.newInstance(context,viewRoot));
    }

    /*
     * getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
     getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
     */
    public static void autoViewValue(Object holder,View viewRoot,Context context) throws Exception{
            Field[] fields = holder.getClass().getDeclaredFields();
            Class c;
            RidName idName;
            Rid id;
            for (Field field : fields) {
                int fieldValue = field.getModifiers();// 获取字段的修饰符
                if (!Modifier.isPublic(fieldValue)) continue; //非公开 跳过
                c = field.getType();
                if (View.class.isAssignableFrom(c)) {
                    //获取注解
                    idName = field.getAnnotation(RidName.class);
                    id = field.getAnnotation(Rid.class);
                    if (id!=null){
                        setViewValue(holder, field.getName(),id.value(), viewRoot);
                    }else if (idName != null) {
                        setViewValue(holder, field.getName(),idName.value(), viewRoot);
                    } else {
                        setViewValue(holder, field.getName(), viewRoot);
                    }
                } else if (ViewHolderAbs.class.isAssignableFrom(c)) {
                    if (context != null) {
                        setViewHolderAbsValue(holder, field.getName(), c, context, viewRoot);
                    }
                }
            }

    }

    public static String reflectionObjectFields(Object object){
        StringBuffer stringBuffer = new StringBuffer();
        reflectionObjectFields(object,stringBuffer,1);
        return stringBuffer.toString();
    }
    private static String[] baseTypes = {
            "java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.String",
            "int","double","long","short","byte","boolean","char","float"};
    private static StringBuffer addTabs(int index,StringBuffer sb){
        for (int i=0;i<index;i++){
            sb.append("\t");
        }
        return sb;
    }
    //打印对象
    private static void reflectionObjectFields(Object object,StringBuffer sb,int level) {
        if (object==null) return;
        Class cla= object.getClass();

        if (cla.isArray()){
            addTabs(level,sb).append("[\n");
            Object[] objects = (Object[]) object;
            if (objects.length == 0 ) return;
            for (Object  obj : objects){
                reflectionObjectFields(obj,sb,level+1);
            }
            addTabs(level,sb).append("]\n");
        }else{
            addTabs(level,sb).append(cla.getName()).append("#").append("{\n");
            Field[] fields=cla.getDeclaredFields();
            for(Field fd : fields){
                if (Modifier.isStatic(fd.getModifiers())) continue;
                try {
                    boolean isBase = false;
                    fd.setAccessible(true);
                    for(String str : baseTypes) {
                        if (fd.getType().getName().equals(str)){
                            addTabs(level, sb).append("(").append(fd.getType().getName()).append(")").append(fd.getName())
                                    .append(" -> ").append(fd.get(object))
                                    .append("\n");
                            isBase = true;
                            break;
                        }
                    }

                    if (!isBase) {
                        addTabs(level,sb).append("(").append(fd.getType().getName()).append(")").append(fd.getName())
                                .append(":");
                                sb.append("\n");
                        reflectionObjectFields(fd.get(object),sb,level+2);
                    }

                } catch (IllegalAccessException e) {
                    addTabs(level,sb).append(e).append("\n");
                }
            }
            addTabs(level,sb).append("}\n");
        }

    }

    public static void autoDestroyViewHolder(Object holder, String methodName) throws Exception {
        Field[] fields = holder.getClass().getDeclaredFields();

            for (Field field : fields) {
            if (ViewHolderAbs.class.isAssignableFrom(field.getType())) {
                Method method = field.getType().getMethod(methodName);
                field.setAccessible(true);//如果是私有的需要先调用setAccessible(true)设置访问权限,
                method.invoke(field.get(holder));
            }
        }
    }


}
