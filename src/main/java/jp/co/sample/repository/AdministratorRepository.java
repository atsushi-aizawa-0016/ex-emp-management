package jp.co.sample.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.domain.Administrator;

/**
 * 管理者情報の挿入とメールアドレスとパスワードから管理者情報を取得するRepository.
 * 
 * @author atsushi
 *
 */
@Repository
public class AdministratorRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Administrator> ADMINISTRATOR_ROW_MAPPER= (rs,i) -> {
		Administrator administrator = new Administrator();
		administrator.setId(rs.getInt("id"));
		administrator.setName(rs.getString("name"));
		administrator.setMailAddress(rs.getString("mail_address"));
		administrator.setPassword(rs.getString("password"));
		return administrator;
	};
	
	/**
	 * 管理者情報を挿入する.
	 * 
	 * @param administrator 挿入したい行情報 
	 */
	public void insert(Administrator administrator) {
		String insertSql = "INSERT INTO administrators (name,mail_address,password) "
						   + "VALUES (:name,:mail_address,:password)";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(administrator);
		
		template.update(insertSql, param);
	}
	
	/**
	 * メールアドレスとパスワードから管理者情報を取得する.
	 * 
	 * @param mail_address
	 * @param password
	 * @return 一人の管理者情報
	 */
	public Administrator findByMailAddressAndPassword(String mail_address,String password) {
		
		String selectSql = "SELECT name,mail_address,password FROM administrators "
						   + "WHERE mail_address=:mail_address AND password=:password";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("mail_address", mail_address).addValue("password", password);
		
		List<Administrator> administratorList = template.query(selectSql, param, ADMINISTRATOR_ROW_MAPPER);
		if (administratorList.size() == 0) {
			return null;
		}else {
			return administratorList.get(0);
		}
	}
}