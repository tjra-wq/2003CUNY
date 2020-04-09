package dev.wsd.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dev.wsd.collections.*;
import dev.wsd.entities.*;
import dev.wsd.services.DataService;
import dev.wsd.utils.*;

/**
 * @author Admin
 * @version 1.0
 * @created 07-Apr-2020 8:56:23 AM
 */
public class AccountTransDAOmaria {

	public AccountTransDAOmaria() {

	}

	public static IAccountTransDAO AccountTransDAOImplementaion = new IAccountTransDAO() {
		@Override
		public AccountTransaction addTransaction(AccountTransaction transObj) {
			// TODO Auto-generated method stub
			// INSERT INTO banksysdb.tbl_accountTransaction (ID, AccountId,
			// TransTypeId,TransAmount, AmountBefore, TransDate, Comment) VALUES(0, NULL,
			// NULL, 0, 0,NULL, NULL);

			AccountTransaction accTrans = null;

			try (Connection con = DBConnectionUtil.openCon();
					PreparedStatement ps = con.prepareStatement(
							"INSERT INTO banksysdb.tbl_accountTransaction (AccountId, TransTypeId, TransAmount, AmountBefore) VALUES(?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);) {
				ps.setInt(1, accTrans.getAccount().getId());
				ps.setInt(2, accTrans.getTransType().getId());
				ps.setInt(3, (int) accTrans.getTransAmount());
				ps.setInt(4, (int) accTrans.getAmountBefore());
				//ps.setDate(5, (Date) accTrans.getTransDate());
				//ps.setString(6, accTrans.getComment());
				ps.execute();
				ResultSet rs = ps.getGeneratedKeys();
				rs.next();// gets first record
				int key = rs.getInt("ID");
				accTrans.setId(key);
				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}
			return accTrans;
		}

		@Override
		public AccountTransCollection<AccountTransaction> viewUserTransactions(int userid) {
			// TODO Auto-generated method stub
			// SELECT ID, AccountId, TransTypeId, TransAmount, AmountBefore, TransDate,
			// Comment FROM banksysdb.tbl_accountTransaction;
			AccountTransCollection<AccountTransaction> tansCollection = new AccountTransCollection<AccountTransaction>();
			AccountTransaction accTrans = null;
			UserAccount userAcount = null;
			TransactionType transType = null;
			try (Connection con = DBConnectionUtil.openCon();
					PreparedStatement ps = con.prepareStatement(
							"SELECT * from tbl_accountTransaction tat where tat.AccountId  in (SELECT ID from tbl_userAccount tua where tua.UserId = ?)");) {
				ps.setInt(1, userid);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					accTrans = new AccountTransaction();
					userAcount = AccountDAOmaria.AccountDAOImplementaion.getAccountById(rs.getInt("AccountId"));
					accTrans.setAccount(userAcount);
					accTrans.setAmountBefore(rs.getFloat("AmountBefore"));
					accTrans.setTransAmount(rs.getFloat("TransAmount"));
					accTrans.setId(rs.getInt("ID"));
					transType = new TransactionType();
					transType = DataService.DataServiceOperation.getTransactionTypeById(rs.getInt("TransTypeId"));
					accTrans.setTransType(transType);
					accTrans.setTransDate(rs.getDate("TransDate"));

					tansCollection.add(accTrans);

				}

				con.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}
			return tansCollection;
		}

	};

}