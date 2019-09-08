package com.jsj.orm.dao;

import com.jsj.orm.BaseMapper;
import com.jsj.orm.UserDO;
import com.jsj.orm.map.*;

import javax.sql.DataSource;
import java.util.ArrayList;

public class UserMapperImpl extends BaseMapper implements UserMapper {
    private static final String createTable = "CREATE TABLE `tb_user` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `user_name` varchar(50) NOT NULL COMMENT '用户名',\n" +
            "  `phone` varchar(20) NOT NULL COMMENT '手机号码',\n" +
            "  `balance` decimal(18,3) NOT NULL COMMENT '现金',\n" +
            "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

    private static final String selectOne = "select * from `tb_user` where id =?";

    private static final String selectName = "select user_name from `tb_user` where id =?";

    private static final String updateName = "update `tb_user` set user_name=? where id =?";

    private static final String updateUserDO = "update `tb_user` set user_name=? , phone=? , balance=? where id =?";

    private static final ResultMapHandler<UserDO> resultMapHandler = new DefaultResultMapHandler<>(UserDO.class,
            new ArrayList<ResultMap>() {{
                add(new ResultMap("id", "id"));
                add(new ResultMap("user_name", "userName"));
                add(new ResultMap("phone", "phone"));
                add(new ResultMap("balance", "balance"));
                add(new ResultMap("create_time", "createTime"));
            }}
    );

    private static final ParamMapHandler<UserDO> paramMapHandler = (userDO1) -> {
        Object[] params = new Object[4];
        params[0] = userDO1.getUserName();
        params[1] = userDO1.getPhone();
        params[2] = userDO1.getBalance();
        params[3] = userDO1.getId();
        return params;
    };

    public UserMapperImpl(DataSource dataSource, boolean autoCommit) {
        super(dataSource, autoCommit);
    }

    @Override
    public UserDO selectOne(Long id) {
        return selectOne(selectOne, resultMapHandler, id);
    }

    @Override
    public boolean updateName(String userName, Long id) {
        return update(updateName, userName, id);
    }

    @Override
    public String selectName(Long id) {
        return selectOne(selectName, new BasicResultMapHandler<>(String.class), id);
    }

    @Override
    public void createTableIfNotExists() {
        update(createTable);
    }

    @Override
    public boolean update(UserDO userDO) {
        return update(updateUserDO, paramMapHandler.mapper(userDO));
    }
}
