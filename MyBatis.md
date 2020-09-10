# MyBatis

## 持久层技术解决方案

1. JDBC技术：
   + Connection
   + PreparedStatement
   + ResultSet
2. Spring的JdbcTemplate：
   + Spring中对jdbc的简单封装
3. Apache的DBUtils：
   + 和Spring的JdbcTemplate类似，也是对jdbc的简单封装

以上均不是框架，JDBC是规范，Spring的JdbcTemplate和Apache的DBUtils是工具类。

+ **使用JDBC存在的一些缺陷**
  + 数据库链接创建、释放频繁造成系统资源浪费从而影响系统性能，如果使用数据库链接池可解决此问题。
  + Sql语句在代码中硬编码，造成代码不易维护，实际应用sql变化的可能较大，sql变动需要改变java代码。
  + 使用preparedStatement向占有位符号传参数存在硬编码，因为sql语句的where条件不一定，可能多也可能少，修改sql还要修改代码，系统不易维护。
  + 对结果集解析存在硬编码（查询列名），sql变化导致解析代码变化，系统不易维护，如果能将数据库记录封装成pojo对象解析比较方便。



## MyBatis框架概述

1. mybatis是一个优秀的基于java的持久层框架，它内部封装了jdbc，使开发者只需要关注sql语句本身，而不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程。 
2. mybatis通过xml或注解的方式将要执行的各种statement配置起来，并通过java对象和statement中sql的动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为java对象并返回。
3. 采用ORM思想解决了实体和数据库映射的问题，对jdbc进行了封装，屏蔽了jdbc api底层访问细节，使我们不用与jdbc api打交道，就可以完成对数据库的持久化操作。
   + ORM（Object Relational Mapping，对象关系映射）：把数据库表和实体类以及实体类的属性对应起来，实现操作实体类就可以操作数据库。



## MyBatis入门

1. 创建maven工程并导入相关依赖

   ```xml
   <dependency>
     <groupId>org.mybatis</groupId>
     <artifactId>mybatis</artifactId>
     <version>x.x.x</version>
   </dependency>
   ```

2. 创建实体类以及对应的dao类，如User类和UserDao类

3. 在resources文件夹下创建MyBatis主配置文件SqlMapConfig.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   
   <!--mybatis主配置文件-->
   <configuration>
       <!-- 配置环境 -->
       <environments default="mysql">
           <!--配置mysql环境-->
           <environment id="mysql">
               <!--配置事务类型-->
               <transactionManager type="JDBC"></transactionManager>
               <!--配置数据源（连接池）-->
               <dataSource type="POOLED">
                   <!--配置连接数据库的四个基本信息-->
                   <property name="driver" value="com.mysql.jdbc.Driver"/>
                   <property name="url" value="jdbc:mysql://localhost:3306/mybatisdb"/>
                   <property name="username" value="root"/>
                   <property name="password" value="123456"/>
               </dataSource>
           </environment>
       </environments>
       <!--指定映射配置文件的位置，映射配置文件指的是每个dao独立的配置文件-->
       <mappers>
           <mapper resource="cn/sucrelt/dao/UserMapper.xml"/>
       </mappers>
   </configuration>
   ```

4. 在resources文件夹下创建对应的目录，并创建映射配置文件UserMapper.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="cn.sucrelt.dao.UserDao" resultType="cn.sucrelt.domain.User">
       <select id="findAll">
           select * from user;
       </select>
   </mapper>
   ```
   
5. 编写测试文件，每个Mybatis应用都是以SqlSessionFactory的实例为核心的，首先将配置文件通过输入流读取，SqlSessionFactoryBuilder从配置中构建出SqlSessionFactory实例，在获取到Factory对象后创建出SqlSession实例，通过SqlSession对象的映射器方法获取对应的实体类并执行操作，最后释放资源。

   ```java
   //1.读取配置文件
   InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
   //2.创建SqlSessionFactory工厂
   SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
   SqlSessionFactory factory = builder.build(in);
   //3.使用工厂生产SqlSession对象
   SqlSession session = factory.openSession();
   //4.使用SqlSession对象创建Dao接口的代理对象
   UserDao userDao = session.getMapper(UserDao.class);
   //5.使用代理对象执行方法
   List<User> users = userDao.findAll();
   for (User user :
           users) {
       System.out.println(user);
   }
   //6.释放资源
   session.close();
   in.close();
   ```

   > SqlSession 在 MyBatis 中是非常强大的一个类。它包含了所有执行语句、提交或回滚事务以及获取映射器实例的方法。

   总结：

   1. 获取配置文件的方式
      + 使用类加载器，这种只能读取类路径的配置文件
      + 使用ServletContext对象的getRealPath()方法
   2. SqlSessionFactory工厂模式的优势在于：
      + 避免了使用new创建新对象，解耦（降低类之间的依赖关系）。
   3. SqlSessionFactoryBuilder创建者模式：
      + 把对象的创建细节隐藏，使使用者直接调用方法即可拿到复杂对象。
   4. session.getMapper(UserDao.class);使用代理模式
      + 使用另一个类实现并增强一个类的功能。
      + :star:getMapper方法的本质：使用Proxy.newProxyInstance()方法生成代理对象MapperProxy类，该类最终会生成一个MappedMethod对象,然后执行其execute()方法,将当前的sqlSession和方法参数传入执行sql，最终还是调用了sqlSession相应的方法进行的查询，比如说最常用的返回多个值就是调用了sqlSession的selectList()方法完成查询。

6. **注意事项**

   + 映射文件的目录需要和对应的Dao接口的包结构相同。
   + 映射文件中mapper标签的namespace属性的取值为对应Dao接口的全限定类名。
   + 映射文件中操作配置标签（select）的id属性的取值为Dao接口的方法名。
   + 映射文件中操作配置标签的resultType属性，表示返回的封装对象的类型。

7. 使用**注解**配置MyBatis

   + 在持久层实体类中添加注解

     ```java
     public interface UserDao {
         /**
          * 查询所有记录
          *
          * @return
          */
         @Select("select * from user")
         List<User> findAll();
     }
     ```

   + 删除实体类对应的xml文件，在主配置文件中的mapper标签设置class属性，代表映射位置，值为对应实体类的全限定类名。

     ```java
     <mappers>
         <mapper class="cn.sucrelt.dao.UserDao"/>
     </mappers>
     ```



## 基本CURD中的注意事项

1. 将测试文件进行功能分离，利用单元测试的`@Before`和`@After`注解，在测试前执行相关初始化生成Session对象的操作，在测试之后，执行**事务提交**`session.commit()`以及关闭连接释放资源的操作。

2. 在执行数据库操作时如果需要保存相关信息：

   ```xml
   <!--保存用户-->
       <insert id="saveUser" parameterType="cn.sucrelt.domain.User">
           <!-- 配置保存时获取插入的id -->
           <selectKey keyColumn="id" keyProperty="id" resultType="int" order="AFTER">
               select last_insert_id();
           </selectKey>
           insert into user(username, birthday, sex, address)
           values (#{username}, #{birthday}, #{sex}, #{address});
       </insert>
   ```

   其中，keyColumn为数据库的列名，keyProperty为实体类对应的属性名，order表示在该数据库操作执行之后获取该值。

3. 在sql语句中使用`#{}`字符代表占位符，相当于jdbc中的`?`，用于具体执行语句时替换实际的参数。 

   + #{}中的内容，使用的是OGNL表达式， 它是apache提供的一种表达式语言，全称是：Object Graphic Navigation Language，即对象图导航语言，是按照一定的语法格式来获取数据的。 
   + 语法格式： #{对象.对象}。
   + #{}中的名称要和实体类中getter/setter方法一致，如果是默认生成的，则与实体类的属性名一致。
   + 如果sql语句中只含有一个占位符表示变量，则变量名任意都可以。

4. 模糊查询时，由于映射文件中不使用%：

   1. 在测试传参时加入%，实际上是对参数进行字符串的拼接

      ```xml
      <!--根据name模糊查询用户-->
      <select id="findByName" parameterType="String" resultType="cn.sucrelt.domain.User">
          select * from user where username like #{username};
      </select>
      ```

      ```java
      /**
       * 测试模糊查询
       */
      @Test
      public void testFindByName() throws IOException {
          //使用代理对象执行方法
         	List<User> users = userMapper.findByName("%王%");
          for (User user :
                  users) {
              System.out.println(user);
          }
      }
      ```

   2. 使用`${value}`代替原来的占位符`#{}`

      ```xml
      <!--根据name模糊查询用户-->
      <select id="findByName" parameterType="String" resultType="cn.sucrelt.domain.User">
          select * from user where username like '%${value}%';
      </select>
      ```

      ```java
      /**
       * 测试模糊查询
       */
      @Test
      public void testFindByName() throws IOException {
          //使用代理对象执行方法
          List<User> users = userMapper.findByName("王");
          for (User user :
                  users) {
              System.out.println(user);
          }
      }
      ```

   3. #{}与${}的区别

      #{}表示一个占位符号，通过#{}可以实现preparedStatement向占位符中设置值，自动进行java类型和jdbc类型转换，可以有效防止sql注入。#{}可以接收简单类型值或pojo属性值。

      ${}表示拼接sql串，通过${}可以将parameterType传入的内容拼接在sql中且不进行jdbc类型转换，${}可以接收简单类型值或pojo属性值。

5. parameterType属性的取值可以是基本类型，引用类型（例如:String类型），还可以是实体类类型（POJO类），也可以使用实体类的包装类。当是实体类的包装类时，使用OGNL表达式的语法获取查询条件进行查询。**一定要写全限定类名。**

   ```xml
   <!-- 根据用户名称模糊查询，参数变成一个QueryVo对象了 -->
   <select id="findByVo" resultType="cn.sucrelt.domain.User" parameterType="cn.sucrelt.domain.QueryVo">
       select *
       from user
       where username like #{user.username};
   </select>
   ```

   > #{user.username}它会先去找user对象（前提是对应类QueryVo中存在getUser方法），然后在user对象中找到username属性，并调用getUsername()方法把值取出来。但是我们在parameterType属性上指定了实体类名称，所以可以省略user.而直接写username。

6. resultType属性可以指定结果集的类型，它支持基本类型和实体类类型。

7. resultMap结果集映射：当实体类中的属性名和数据库中的列名不一致时，可以使用resultMap做属性名的映射。

   ```xml
   <resultMap type="cn.sucrelt.domain.User" id="userMap">
       <!--主键字段的对应-->
       <id column="id" property="userId"/>
       <!--非主键字段的对应-->
       <result column="username" property="userName"/>
       <result column="sex" property="userSex"/>
       <result column="address" property="userAddress"/>
       <result column="birthday" property="userBirthday"/>
   </resultMap>
   <!--查询所有-->
       <select id="findAll" resultMap="userMap">
           select *
           from user;
       </select>
   ```

   + **resultType和resultMap只能同时使用一个。**
   + resultMap的映射**仅限于在结果集中**。在sql拼接中，如#{}中的变量名，应当与实体类中属性名一致，不受该映射关系影响。



## 连接池

### 连接池特点

1. 连接池是用于存储连接的容器
2. 容器其实是一个集合对象，需要保证是线程安全的，不允许两个线程拿到一个连接
3. 连接池必须实现队列的特性：先进先出

### MyBatis中的连接池

1. MyBatis提供三种方式的配置

   + 配置位置：主配置文件SqlMapConfig的dataSource标签中

     ```xml
     <dataSource type="POOLED">
         <!--配置连接数据库的四个基本信息-->
         <property name="driver" value="${jdbc.driver}"/>
         <property name="url" value="${jdbc.url}"/>
         <property name="username" value="${jdbc.username}"/>
         <property name="password" value="${jdbc.password}"/>
     </dataSource>
     ```

   + type属性的取值：

     + POLLED 采用传统的javax.sql.DataSource中的连接池
     + UNPOOLED 采用传统的获取连接的方式，实现了javax.sql.DataSource中的接口，但没有使用池的思想
     + JNDI 采用服务器提供的JNDI技术实现，来获取DataSource对象

2. MyBatis内部分别定义了实现了java.sql.DataSource接口的UnpooledDataSource，PooledDataSource类来表示UNPOOLED、POOLED类型的数据源。MyBatis在初始化时，根据<dataSource>的type属性来创建相应类型的的数据源DataSource，即： 
   + type=”POOLED”：MyBatis会创建PooledDataSource实例 
   + type=”UNPOOLED” ： MyBatis会创建UnpooledDataSource实例 
   + type=”JNDI”：MyBatis会从JNDI服务上查找DataSource实例，然后返回使用



## 事务

在MyBatis中提交事务，实际上就是调用JDBC的`setAutoCommit()`来实现事务控制。

1. 在CUD的操作中，需要对事务进行提交，由于SqlSession对象在从连接池获取连接时，都会执行`connection.setAutoCommit(false)`方法，因此事务的提交需要手动进行，使用`sqlSession.commit()`方法，相当于使用了JDBC中的`connection.commit()`方法实现事务提交。
2. 在调用factory对象的`openSession()`方法时，可以使用`session = factory.openSession(true)`，这样就可以设置该SqlSession对象中的事务全部为自动提交，不需要手动执行`sqlSession.commit()`方法。



## 动态Sql

1. if标签（**注意使用where1=1**）

   ```xml
   <select id="findUserByCondition" resultMap="userMap" parameterType="cn.sucrelt.domain.User">
       select * from user1 where 1=1
       <if test="userName!=null">
           and username = #{userName}
       </if>
       <if test="userSex!=null">
           and sex = #{userSex}
       </if>
   </select>
   ```

2. where标签（**无需使用where1=1**）

   ```xml
   <select id="findUserByCondition" resultMap="userMap" parameterType="cn.sucrelt.domain.User">
       select * from user1
       <where>
           <if test="userName!=null">
               and username = #{userName}
           </if>
           <if test="userSex!=null">
               and sex = #{userSex}
           </if>
       </where>
   </select>
   ```

3. foreach标签

   ```xml
   <select id="findUserByIds" resultMap="userMap" parameterType="cn.sucrelt.domain.QueryVo">
       select * from user1
   <where>
       <if test="ids!=null and ids.size()>0">
           <foreach collection="ids" open="and id in (" close=")" item="id" separator=",">
               #{id}
           </foreach>
       </if>
   </where>
   </select>
   ```

   foreach标签中，collection表示遍历的集合，open表示拼接到sql语句中开始的部分，close表示结束的部分，separator表示中间分隔符，item表示遍历到的每个元素，在标签体中#{}中的内容与item一致。
   
   

## 多表查询

### 一对一查询

查询要求：查询Account表和User表中的信息，Account表中存在外键UID对应User表中的主键id，此时查询的结果集包含Account表和User表中的部分信息。

```sql
SELECT account.*, user.username, user.address
FROM account,
     user
WHERE user.id = account.UID
```

该一对一多表查询的结果集需要另作处理。

1. 通过扩展从表实体类封装结果集

   新建一个类AccountUser，继承Account类并扩展，在其中加入对应User表中的属性username和address，执行SQl时的结果集设置为该类即可。

   **即定义专门的po类作为输出类型，其中定义了sql查询结果集所有的字段。此方法较为简单，企业中使用普遍。**

   ```xml
   <select id="findAllAccount" resultType="cn.sucrelt.domain.AccountUser">
       SELECT account.*, user.username, user.address
       FROM account,
            user
       WHERE user.id = account.UID
   </select>
   ```

2. 通过resultMap，定义专门的resultMap用于映射一对一查询结果。

   在Account从表实体类中包含一个主表实体User的对象引用，并生成对应的getter和setter方法。

   ```java
   public class Account implements Serializable {
       private Integer id;
       private Integer uid;
       private Double money;
   
       /**
        * 从表实体包含一个主表实体的对象引用
        */
       private User user;
       ...
   ```

   定义一个resultMap，设置Account表与实体类的映射关系，这里将表中的id设置为aid用于区分user表中的id。然后使用`association`标签将User表及其对应的实体类属性做映射。

   **注意**：在sql语句的编写中，查询User表所有信息（`u.*`），以及Account表中的所有信息，其中将`a.id`设置为`aid`。

   ```xml
   <!--定义封装Account和User的resultMap-->
   <resultMap id="accountUserMap" type="cn.sucrelt.domain.Account">
       <id column="aid" property="id"/>
       <result column="uid" property="uid"/>
       <result column="money" property="money"/>
       <!-- 它是用于指定从表方的引用实体属性 -->
       <association property="user" javaType="cn.sucrelt.domain.User">
           <id column="id" property="userId"/>
           <result column="username" property="userName"/>
           <result column="sex" property="userSex"/>
           <result column="birthday" property="userBirthday"/>
           <result column="address" property="userAddress"/>
       </association>
   </resultMap>
   
   <!--查询所有账户，同时包含用户名和地址，第二种方式-->
       <select id="findAllAccount2" resultMap="accountUserMap">
           select u.*, a.id as aid, a.uid, a.money
           from account a,
                user u
           where u.id = a.uid
       </select>
   ```



### 一对多查询

查询user表中的每个用户信息以及用户包含的所有账户信息

```sql
select u.*, a.id as aid, a.uid, a.money
from user u
left outer join account a on u.id = a.uid
```

在主表User对应的实体类User中加入从表实体类Account的集合引用

```java
public class User implements Serializable {
    private Integer userId;
    private String userName;
    private Date userBirthday;
    private String userSex;
    private String userAddress;

    //一对多关系映射。主表实体应该包含从表实体的集合引用
    private List<Account> accounts;
    ...
```

定义resultMap用于封装结果集，对应加入Account集合引用的User实体类

```xml
<!--定义加入Account集合的User对象的resultMap-->
<resultMap id="userAccountMap" type="cn.sucrelt.domain.User">
    <id property="userId" column="id"></id>
    <result column="username" property="userName"/>
    <result column="address" property="userAddress"/>
    <result column="sex" property="userSex"/>
    <result column="birthday" property="userBirthday"/>
    <!-- collection是用于建立一对多中集合属性的对应关系 property为主表对应实体类中的属性 ofType用于指定集合元素的数据类型 -->
    <collection property="accounts" ofType="cn.sucrelt.domain.Account">
        <id column="aid" property="id"/>
        <result column="uid" property="uid"/>
        <result column="money" property="money"/>
    </collection>
</resultMap>

<!-- 根据用户查询所有账户 -->
<select id="findAllAccountByUser" resultMap="userAccountMap">
    select u.*, a.id as aid, a.uid, a.money
    from user u
    left outer join account a on u.id = a.uid
</select>
```



### 多对多查询

在数据库中要让表具有多对多的关系，需要使用中间表

查询身份表role中所有信息，对应user表中的用户信息，中间表为user_role

```sql
select u.*, r.id as rid, r.role_name, r.role_desc
from role r
         left outer join user_role ur on r.id = ur.rid
         left outer join user u on u.id = ur.uid
```

在role实体类中，加入多对多关系映射

```java
public class Role implements Serializable {
    private Integer roleId;
    private String roleName;
    private String roleDesc;


    //多对多的关系映射
    private List<User> users;
    ...
```

使用封装的结果集执行sql

```xml
<!--定义role表的ResultMap-->
<resultMap id="roleMap" type="cn.sucrelt.domain.Role">
    <id property="roleId" column="rid"></id>
    <result property="roleName" column="role_name"></result>
    <result property="roleDesc" column="role_desc"></result>
    <collection property="users" ofType="cn.sucrelt.domain.User">
        <id column="id" property="userId"></id>
        <result column="username" property="userName"></result>
        <result column="address" property="userAddress"></result>
        <result column="sex" property="userSex"></result>
        <result column="birthday" property="userBirthday"></result>
    </collection>
</resultMap>
<!--查询所有-->
<select id="findAll" resultMap="roleMap">
    select u.*, r.id as rid, r.role_name, r.role_desc
    from role r
             left outer join user_role ur on r.id = ur.rid
             left outer join user u on u.id = ur.uid
</select>
```

查询的结果将会返回结果集roleMap类型的数据，对应于包含User实体类的Role实体类对象。

同理，可以查询用户表User中每个用户所对应的身份表Role中的信息，实现User表和Role的多对多查询操作。



## 延迟加载

1. 延迟加载：为了提高数据库的查询性能，尽量使用单表查询，如果需要关联数据时才进行多表联合查询。

   一对多，多对多：一般采用延迟加载；

   一对一，多对一：一般采用立即加载。

2. 在配置文件SqlMapConfig.xml中设置延迟加载参数

   + lazyLoadingEnabled：如果为false，则所有相关联的都会被初始化加载；
   + aggressiveLazyLoading：如果为true，

   ```xml
   <!-- 全局配置参数 -->
   <settings>
   <!-- 延迟加载总开关 -->
   <setting name="lazyLoadingEnabled" value="true" />
   <!-- 设置按需加载 -->
   <setting name="aggressiveLazyLoading" value="false" />
   </settings>
   ```

3. 使用association实现：查询账户信息，并查询对应的用户信息

   1. 根据之前的方式，使用resultMap配置结果集，在其中使用association标签配置延迟查询

      ```xml
      <!--定义封装Account和User的resultMap-->
      <resultMap id="accountUserMap" type="cn.sucrelt.domain.Account">
          <id column="aid" property="id"/>
          <result column="uid" property="uid"/>
          <result column="money" property="money"/>
          <!-- 配置延迟加载 -->
          <association property="user" javaType="cn.sucrelt.domain.User" 
                       select="cn.sucrelt.dao.UserMapper.findUserById" column="uid">
          </association>
      </resultMap>
      
      <select id="findAll" resultMap="accountMap"> 
          select * from account 
      </select>
      
      ```

      select属性：指定当需要延迟加载时执行的select映射位置，此处为查询用户的唯一标识。

      column属性：执行select属性中的的查询所需的参数，对应为uid，即account中查询出来的uid值，延迟加载时根据此值去user表中查询。

   2. 在user表对应的映射文件中设置findUserById

      ```xml
      <mapper namespace="cn.sucrelt.dao.UserMapper"> 
      <!-- 根据id查询 --> 
      <select id="findUserById" resultType="cn.sucrelt.domain.User" parameterType="int" > 
      select * from user where id = #{uid} 
      </select> 
      </mapper>
      ```

   3. 在测试结果时

      ```java
      @Test 
      public void testFindAll() { 
      	//6.执行操作 
      	List<Account> accounts = accountDao.findAll(); 
          System.out.println(accounts.get(0).getUser());
      }
      ```

      因为使用了延迟加载，所以只查询所有时并不会显示user信息，当获取user信息是才会进行延迟加载显示user信息。

4. 使用collection实现：查询用户信息，并查询对应的所有账户信息

   1. 使用collection标签的配置如下：

      ```xml
      <resultMap id="userAccountMap" type="cn.sucrelt.domain.User">
          <id property="userId" column="id"></id>
          <result column="username" property="userName"/>
          <result column="address" property="userAddress"/>
          <result column="sex" property="userSex"/>
          <result column="birthday" property="userBirthday"/>
         
          <collection property="accounts" ofType="cn.sucrelt.domain.Account" 
                      select="cn.sucrelt.dao.AccountMapper.findByUid" column="id">
          </collection>
      </resultMap>
      
      <select id="findAll" resultMap="userMap"> 
          select * from user 
      </select> 
      ```

      + select属性： 用于指定查询account列表的sql语句，所以填写的是该sql映射的id 
      + column属性： 用于指定select属性的sql语句的参数来源，上面的参数来自于user的id列，所以就写成id这一个字段名了

   2. account对应配置文件中

      ```xml
      <!-- 根据用户id查询账户信息 --> 
      <select id="findByUid" resultType="account" parameterType="int"> 
          select * from account where uid = #{uid} 
      </select>
      ```

   3. 在执行查询用户信息时，只有执行了user对象的getAccount()方法，才会延迟加载显示account信息。



## 缓存

### 一级缓存

1. 一级缓存是SqlSession范围的缓存，如执行了查询某一user对象，再次查询时将会在缓存中查询，当调用SqlSession的修改，添加，删除，commit()，close()等方法时，就会清空一级缓存。
2. sqlSession.clearCache();//此方法也可以清空缓存。



### 二级缓存

1. 二级缓存是mapper映射级别的缓存，多个SqlSession去操作同一个Mapper映射的sql语句，多个SqlSession可以共用二级缓存，二级缓存是跨SqlSession的。

2. 第一步：在SqlMapConfig.xml文件开启二级缓存

   ```xml
   <settings> 
   <!-- 开启二级缓存的支持 --> 
   <setting name="cacheEnabled" value="true"/> 
   </settings>
   ```

3. 第二步：配置相关的Mapper映射文件

   ```xml
   <cache>标签表示当前这个mapper映射将使用二级缓存，区分的标准就看mapper的namespace值。 
   <?xml version="1.0" encoding="UTF-8"?> 
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"> 
   <mapper namespace="cn.sucrelt.dao.UserMapper"> 
   <!-- 开启二级缓存的支持 --> 
   <cache></cache> 
   </mapper>
   ```

4. 第三步：配置statement上面的useCache属性

   ```xml
   <!-- 根据id查询 --> 
   <select id="findById" resultType="user" parameterType="int" useCache="true"> 
   select * from user where id = #{uid} 
   </select> 
   ```

   将UserDao.xml映射文件中的<select>标签中设置useCache=”true”代表当前这个statement要使用二级缓存，如果不使用二级缓存可以设置为false.

   **注意**：针对每次查询都需要最新的数据sql，要设置成useCache=false，禁用二级缓存。

5. 使用二级缓存时，所缓存的类一定要实现java.io.Serializable接口，这种就可以使用序列化方式来保存对象。



## 注解开发

Mybatis也可以使用注解开发方式，这样可以减少编写Mapper映射文件。

常用注解如下：

```java
@Insert:实现新增 
@Update:实现更新 
@Delete:实现删除 
@Select:实现查询 
@Result:实现结果集封装 
@Results:可以与@Result一起使用，封装多个结果集 
@ResultMap:实现引用@Results定义的封装 
@One:实现一对一结果集封装 
@Many:实现一对多结果集封装 
@SelectProvider: 实现动态SQL映射 
@CacheNamespace:实现注解二级缓存的使用
```

例如，在UserMapper中使用注解设置查询所有用户的方法如下，如此不需要使用xml对该类进行映射。

```java
 /**
     * 查询所有用户
     * @return
     */
    @Select("select * from user")
    @Results(id="userMap",value={
            @Result(id=true,column = "id",property = "userId"),
            @Result(column = "username",property = "userName"),
            @Result(column = "address",property = "userAddress"),
            @Result(column = "sex",property = "userSex"),
            @Result(column = "birthday",property = "userBirthday"),
            @Result(property = "accounts",column = "id",
                    many = @Many(select = "cn.sucrelt.dao.AccountMapper.findAccountByUid",
                                fetchType = FetchType.LAZY))
    })
    List<User> findAll();
```

