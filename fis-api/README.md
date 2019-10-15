FIS publication offical endpoint: 
https://experts.colorado.edu/es/webex-v3/_search

Mappings showing list of all fields in the index:
https://experts.colorado.edu/es/webex-v3/_mapping

CU Experts page showing all publications for an organization - eg LASP
https://experts.colorado.edu/publications?source_content_type=application%2Fjson&source=%7B%22query%22%3A%7B%22bool%22%3A%7B%22must%22%3A%5B%7B%22term%22%3A%7B%22authors.organization.name.keyword%22%3A%22Laboratory%20for%20Atmospheric%20and%20Space%20Physics%20(LASP)%22%7D%7D%2C%7B%22match_all%22%3A%7B%7D%7D%5D%7D%7D%2C%22sort%22%3A%5B%7B%22publicationYear.keyword%22%3A%7B%22order%22%3A%22desc%22%7D%7D%5D%2C%22from%22%3A0%2C%22size%22%3A20%7D

The actual elastic query string to search for all publications by organization eg: LASP publications:
{"query":{"bool":{"must":[{"term":{"authors.organization.name.keyword":"Laboratory for Atmospheric and Space Physics (LASP)"}},{"match_all":{}}]}},"sort":[{"publicationYear.keyword":{"order":"desc"}}],"from":0,"size":20}

This query can be translated to this URL encoded string which goes directly to the elastic endpoint
https://experts.colorado.edu/es/webex-v3/_search?source_content_type=application%2Fjson&source=%7B%22query%22%3A%7B%22bool%22%3A%7B%22must%22%3A%5B%7B%22term%22%3A%7B%22authors.organization.name.keyword%22%3A%22Laboratory%20for%20Atmospheric%20and%20Space%20Physics%20(LASP)%22%7D%7D%2C%7B%22match_all%22%3A%7B%7D%7D%5D%7D%7D%2C%22sort%22%3A%5B%7B%22publicationYear.keyword%22%3A%7B%22order%22%3A%22desc%22%7D%7D%5D%2C%22from%22%3A0%2C%22size%22%3A20%7D

Clean json query string used by publications page to query all publications and aggregations:
{ 
   "query":{ 
      "match_all":{ 

      }
   },
   "sort":[ 
      { 
         "publicationYear.keyword":{ 
            "order":"desc"
         }
      }
   ],
   "from":0,
   "size":20,
   "aggs":{ 
      "mostSpecificType.keyword":{ 
         "terms":{ 
            "field":"mostSpecificType.keyword",
            "size":115,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "authors.name.keyword":{ 
         "terms":{ 
            "field":"authors.name.keyword",
            "size":120,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "publishedIn.name.keyword":{ 
         "terms":{ 
            "field":"publishedIn.name.keyword",
            "size":115,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "publicationYear.keyword":{ 
         "terms":{ 
            "field":"publicationYear.keyword",
            "size":125,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "authors.organization.name.keyword":{ 
         "terms":{ 
            "field":"authors.organization.name.keyword",
            "size":115,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "authors.researchArea.name.keyword":{ 
         "terms":{ 
            "field":"authors.researchArea.name.keyword",
            "size":115,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "cuscholarexists.keyword":{ 
         "terms":{ 
            "field":"cuscholarexists.keyword",
            "size":115,
            "order":{ 
               "_count":"desc"
            }
         }
      },
      "amscore":{ 
         "range":{ 
            "field":"amscore",
            "ranges":[ 
               { 
                  "to":0.001
               },
               { 
                  "to":100,
                  "from":0.001
               },
               { 
                  "to":500,
                  "from":100
               },
               { 
                  "from":500
               }
            ]
         }
      }
   }
}


Javascript library we use to build our people and publcations page from Elastic:
https://github.com/vivo-community/facetedsearch

Elastic reference: https://www.elastic.co/guide/en/elasticsearch/reference/6.7/index.html
