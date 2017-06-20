## Process##

1. 使用dom4j对contex xml配置文件进行解析，装换成Java对象，可以使用Map数据结构进行保存

   ```java
   public class Bean() {
     	private String name;
       private String className;
       private String scope="singleton";
       private List<Property> properties=new ArrayList<Property>();
   }   

   public class Propertie(){
     private String name;
     private String value
     private String ref;
   }
   ```

2. 将xml配置文件map完成之后，对找到的所有bean节点进行遍历，使用Java反射将实例创建出来。

3. 如果有properties，就遍历properties，并且有value的话，就使用BeanUtils.populate将value map到相应的字段上面去，如果有ref的值的话，就通过递归方式创建出对象，然后使用BeanUtils.setProperty将ref属性map到新建立的对象上面去

   ```java
    private Map<String, Object> context = new HashMap<String, Object>();

       public Object getBean(String name) {
           Object bean = context.get(name);
           if (bean == null) {
               bean = createBean(map.get(name));
           }

           return bean;
       }
   ```

   ​

