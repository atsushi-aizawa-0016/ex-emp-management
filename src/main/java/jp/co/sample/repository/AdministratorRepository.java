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
 * 管理者情報の挿入とメールアドレスとパスワードから管理者情報を取得するメソッドを定義したクラス.(ダメな例）
 * 
 * administratorsテーブルを操作するリポジトリ.
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
	 * @param administrator 挿入したい管理者情報 
	 */
	public void insert(Administrator administrator) {
		String insertSql = "INSERT INTO administrators (name,mail_address,password) "
						   + "VALUES (:name,:mailAddress,:password)";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(administrator);
		
		template.update(insertSql, param);
	}
	
	/**
	 * メールアドレスとパスワードから管理者情報を取得する.
	 * 
	 * @param mail_address　メールアドレス情報
	 * @param password パスワード情報
	 * @return 管理者情報 (もし存在しなければnullが返る)
	 */
	public Administrator findByMailAddressAndPassword(String mailAddress,String password) {
		
		String selectSql = "SELECT name,mail_address,password FROM administrators "
						   + "WHERE mail_address=:mailAddress AND password=:password";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress).addValue("password", password);
		
		List<Administrator> administratorList = template.query(selectSql, param, ADMINISTRATOR_ROW_MAPPER);
		if (administratorList.size() == 0) {
			return null;
		}else {
			return administratorList.get(0);
		}
	}
}
