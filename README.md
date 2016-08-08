Phenotips HPO TermRequester
===========================

This service is designed to request that new phenotypes be added to the HPO and give a handle
to requested phenotypes whilst they're being considered for inclusion.

REST API
========

DATA STRUCTURES
---------------

### `phenotype`

```javascript
{
  'id': 'TEMPHPO_...',           /* The id of this phenotype within the request service */
  'name': '...',
  'status': '...', /* See the status below */
  'synonyms': ['...', '...', ],
  'description': '...',
  'issueNumber': '...',           /* The github issue number in the HPO's github */
  'parents': ['...', ], /* The ids of this phenotype's parents */
  'hpoId': '...', /* The id of the term in the HPO. Non-null iff status == ACCEPTED */
}
```

### `status`

One of:

- `SUBMITTED`
- `REJECTED`
- `ACCEPTED`
- `SYNONYM`

CREATE
------

### `POST /phenotypes`

Create a new phenotype request.
If one already exists with that same name or as a synonym of this one, none will be
created, and the previously existing one will be returned.

###### Parameters

These are an exact match of the phenotype data type, only missing the `status`, `id` and `issue_num`.

```javascript
{
  'name': '...',
  'synonyms': ['...', '...', ],
  'description': '...',
  'parents': ['...', '...', ],
}
```

###### Response

If the phenotype has already been requested, the response code will be `HTTP 409`.

In either case the new (or previously existing) phenotype will be returned
(see above for phenotype object format).

READ
----

### `GET /phenotype/{id}`

Where `id` is either a `TEMPHPO_` style id or an `HPO_` type id.

###### Response

If the phenotype exists and is in any status other than `SYNONYM`, it will be returned.
If the phenotype exists but is in `SYNONYM` status, a redirect will be returned to the now-accepted term.
If the phenotype does not exist, an `HTTP 404` will be returned.
If the id is malformed, an `HTTP 400` will be returned.

### `GET /phenotypes`

Search the phenotypes that match a given text.

###### Parameters

```javascript
{
  'text': '...',
}
```

###### Response

```javascript
[{...}, ...] /* the results as phenotype instances */
```

CONFIGURATION
=============

In order to run the webapp, you need to edit its web.xml. In particular, these parameters must
be set on the TermRequester servlet:

```xml
    <init-param>
      <param-name>org.phenotips.termrequester.repositoryOwner</param-name>
      <param-value></param-value>
    </init-param>
    <init-param>
      <param-name>org.phenotips.termrequester.repositoryName</param-name>
      <param-value></param-value>
    </init-param>
    <init-param>
      <param-name>org.phenotips.termrequester.oauthToken</param-name>
      <param-value></param-value>
    </init-param>
    <init-param>
      <param-name>org.phenotips.termrequester.homeDir</param-name>
      <param-value></param-value>
    </init-param>
```

DEVELOPMENT
===========

To develop for the termrequester, you should:

- build and install the phenotips variant store
- populate `backend/src/test/resources/org/phenotips/termrequester/github/credentials.properties`
  by copying `credentials.template.properties` in that same directory
