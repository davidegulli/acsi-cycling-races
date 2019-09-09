import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAcsiTeam, defaultValue } from 'app/shared/model/acsi-team.model';

export const ACTION_TYPES = {
  SEARCH_ACSITEAMS: 'acsiTeam/SEARCH_ACSITEAMS',
  FETCH_ACSITEAM_LIST: 'acsiTeam/FETCH_ACSITEAM_LIST',
  FETCH_ACSITEAM: 'acsiTeam/FETCH_ACSITEAM',
  FETCH_ACSITEAM_BY_USER_LOGGED: 'acsiTeam/FETCH_ACSITEAM_BY_USER_LOGGED',
  CREATE_ACSITEAM: 'acsiTeam/CREATE_ACSITEAM',
  UPDATE_ACSITEAM: 'acsiTeam/UPDATE_ACSITEAM',
  DELETE_ACSITEAM: 'acsiTeam/DELETE_ACSITEAM',
  RESET: 'acsiTeam/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAcsiTeam>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AcsiTeamState = Readonly<typeof initialState>;

// Reducer

export default (state: AcsiTeamState = initialState, action): AcsiTeamState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACSITEAMS):
    case REQUEST(ACTION_TYPES.FETCH_ACSITEAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACSITEAM):
    case REQUEST(ACTION_TYPES.FETCH_ACSITEAM_BY_USER_LOGGED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACSITEAM):
    case REQUEST(ACTION_TYPES.UPDATE_ACSITEAM):
    case REQUEST(ACTION_TYPES.DELETE_ACSITEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACSITEAMS):
    case FAILURE(ACTION_TYPES.FETCH_ACSITEAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACSITEAM):
    case FAILURE(ACTION_TYPES.CREATE_ACSITEAM):
    case FAILURE(ACTION_TYPES.UPDATE_ACSITEAM):
    case FAILURE(ACTION_TYPES.DELETE_ACSITEAM):
    case FAILURE(ACTION_TYPES.FETCH_ACSITEAM_BY_USER_LOGGED):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACSITEAMS):
    case SUCCESS(ACTION_TYPES.FETCH_ACSITEAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACSITEAM):
    case SUCCESS(ACTION_TYPES.FETCH_ACSITEAM_BY_USER_LOGGED):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACSITEAM):
    case SUCCESS(ACTION_TYPES.UPDATE_ACSITEAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACSITEAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/acsi-teams';
const apiSearchUrl = 'api/_search/acsi-teams';

// Actions

export const getSearchEntities: ICrudSearchAction<IAcsiTeam> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ACSITEAMS,
  payload: axios.get<IAcsiTeam>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IAcsiTeam> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACSITEAM_LIST,
    payload: axios.get<IAcsiTeam>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAcsiTeam> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACSITEAM,
    payload: axios.get<IAcsiTeam>(requestUrl)
  };
};

export const getEntityByUserLogged: ICrudGetAction<IAcsiTeam> = () => {
  const requestUrl = `${apiUrl}/by-user-logged`;
  return {
    type: ACTION_TYPES.FETCH_ACSITEAM_BY_USER_LOGGED,
    payload: axios.get<IAcsiTeam>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAcsiTeam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACSITEAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAcsiTeam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACSITEAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAcsiTeam> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACSITEAM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
