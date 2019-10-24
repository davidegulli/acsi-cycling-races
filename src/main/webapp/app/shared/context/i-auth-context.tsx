export default interface IAuthContext {
  isAuthenticated: boolean;
  isAcsiAdmin: boolean;
  isAdmin: boolean;
  isTeamManager: boolean;
  acsiTeam: any;
}
