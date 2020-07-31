/*-
 * #%L
 * Liquibase extension for Clickhouse
 * %%
 * Copyright (C) 2020 Mediarithmics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package liquibase.ext.clickhouse.sqlgenerator;

import liquibase.ext.clickhouse.database.ClickHouseDatabase;

import liquibase.database.Database;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.CreateDatabaseChangeLogTableGenerator;
import liquibase.statement.core.CreateDatabaseChangeLogTableStatement;

public class CreateDatabaseChangeLogTableClickHouse extends CreateDatabaseChangeLogTableGenerator {

  @Override
  public int getPriority() {
    return PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(CreateDatabaseChangeLogTableStatement statement, Database database) {
    return database instanceof ClickHouseDatabase;
  }

  @Override
  public Sql[] generateSql(
      CreateDatabaseChangeLogTableStatement statement,
      Database database,
      SqlGeneratorChain sqlGeneratorChain) {
    String createTableQuery =
        String.format(
            "CREATE TABLE %s.%s ("
                + "ID String,"
                + "AUTHOR String,"
                + "FILENAME String,"
                + "DATEEXECUTED DateTime64,"
                + "ORDEREXECUTED UInt64,"
                + "EXECTYPE String,"
                + "MD5SUM Nullable(String),"
                + "DESCRIPTION Nullable(String),"
                + "COMMENTS Nullable(String),"
                + "TAG Nullable(String),"
                + "LIQUIBASE Nullable(String),"
                + "CONTEXTS Nullable(String),"
                + "LABELS Nullable(String),"
                + "DEPLOYMENT_ID Nullable(String)) "
                + "ENGINE MergeTree() ORDER BY ID",
            database.getDefaultSchemaName(), database.getDatabaseChangeLogTableName());

    return SqlGeneratorUtil.generateSql(database, createTableQuery);
  }
}
