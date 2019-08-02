package jp.co.sample.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.domain.Employee;

/**
 * 従業員一覧取得、主キーから従業員情報取得、従業員情報を変更するメソッドを定義したクラス.(ダメな例)
 * 
 * Employeeテーブルを操作するリポジトリ.
 * 
 * @author atsushi
 *
 */
@Repository
public class EmployeeRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER= (rs,i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hireDate"));
		employee.setMailAddress(rs.getString("mailAddress"));
		employee.setZipCode(rs.getString("zipCode"));
		employee.setAdderss(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependentsCount"));
		return employee;
	};
	
	/**
	 * 従業員一覧情報を入社日順で取得する.
	 * 
	 * @return 従業員一覧情報のリスト
	 */
	public List<Employee> findAll(){
		String findSql = "SELECT id,name,image,gender,hireDate,mailAddress,zipCode,address,telephone,salary,characteristics,dependentsCount "
					 + "FROM employees ORDER BY hireDate";
		
		List<Employee> employeesList = template.query(findSql, EMPLOYEE_ROW_MAPPER);
		
		return employeesList;
	}
	
	/**
	 * 主キーから従業員情報を取得する.
	 * 
	 * @param id 主キー情報
	 * @return 一人の従業員情報
	 */
	public Employee load(Integer id) {
		String loadSql = "SELECT id,name,image,gender,hireDate,mailAddress,zipCode,address,telephone,salary,characteristics,dependentsCount "
						 + "FROM employees WHERE id=:id";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		
		Employee employee = new Employee();
//		try {
			employee = template.queryForObject(loadSql, param, EMPLOYEE_ROW_MAPPER);
//		} catch (Exception e) {
//			System.out.println("例外が発生しました。");
//			e.printStackTrace();
//		}
		return employee;
	}
	/**
	 * 従業員情報を変更する.
	 * 
	 * @param employee　変更する従業員情報
	 */
	public void update(Employee employee) {
		String updateSql = "UPDATE employee SET dependentsCount=:dependentsCount WHERE id=:id";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
		
		template.update(updateSql, param);
	}
}
