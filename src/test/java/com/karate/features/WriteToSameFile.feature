Feature: Write Status Back to SAME Input File

@same-file
Scenario: Update original CSV file with status
    * def inputFile = 'src/test/java/com/karate/data/testdata.csv'
    * def csvData = karate.readAsString('classpath:com/karate/data/testdata.csv')
    * def lines = csvData.split('\n')
    * def header = lines[0]
    * def rows = []
    * eval for(var i = 1; i < lines.length; i++) { if(lines[i].trim() !== '') rows.push(lines[i]) }
    * def totalRows = rows.length
    * print 'Total Rows to process:', totalRows
    
    * def updatedRows = []
    * eval for(var i = 0; i < rows.length; i++) { var columns = rows[i].split(','); var status = (columns[1] === 'Y') ? 'PROCESSED' : 'SKIPPED'; var timestamp = java.time.LocalDateTime.now().toString(); updatedRows.push(rows[i] + ',' + status + ',' + timestamp); }
    
    * def newHeader = header + ',Status,ProcessedAt'
    * def finalContent = newHeader + '\n' + updatedRows.join('\n')
    * karate.write(finalContent, inputFile)
    * print 'Updated CSV file with', updatedRows.length, 'rows'
