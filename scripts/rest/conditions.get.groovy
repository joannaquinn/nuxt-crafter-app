import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.SortOrder

def result = [:]

def queryStatement = 'content-type:"/component/condition"'

def builder = new SearchSourceBuilder().query(QueryBuilders.queryStringQuery(queryStatement))

// execute query
def executedQuery = elasticsearch.search(new SearchRequest().source(builder))

result.conditions = []

def elasticResults = executedQuery.hits.hits*.getSourceAsMap()
elasticResults.eachWithIndex { document, idx ->
	def condition = [ id:             idx,
    				cmsId:          document.localId,
                    title:          document.title_s, 
                    description:    document.description_t,
                  ]
    
	result.conditions.add(condition)
}

return result



def getDesc(document) {
	// simple example of abstracting where price comes from
	return new Float(document.description_t)
}

def getInventory(document) {
	// simple example of abstracting where inventory comes from
	return new Integer(document.installments)
}
    